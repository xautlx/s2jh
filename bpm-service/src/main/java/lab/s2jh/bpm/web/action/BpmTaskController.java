package lab.s2jh.bpm.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.bpm.service.ActivitiService;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.web.view.OperationResult;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.RestActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ModelDriven;

public class BpmTaskController extends RestActionSupport implements ModelDriven<Object> {

    private final static String DYNA_FORM_KEY = "/bpm/bpm-task!dynaForm";

    private Object model;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FormService formService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    protected ActivitiService activitiService;

    @Override
    public Object getModel() {
        return model;
    }

    private Map<String, Object> packageTaskInfo(Task task) {

        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).singleResult();

        Map<String, Object> singleTask = new HashMap<String, Object>();
        singleTask.put("id", task.getId());
        singleTask.put("name", task.getName());
        singleTask.put("createTime", task.getCreateTime());
        singleTask.put("pdname", processDefinition.getName());
        singleTask.put("pdversion", processDefinition.getVersion());
        singleTask.put("pid", task.getProcessInstanceId());
        singleTask.put("bizKey", processInstance.getBusinessKey());

        return singleTask;
    }

    /**
     * 用户待办任务列表
     * @return
     */
    public HttpHeaders userTasks() {
        // 已经签收的任务
        String userpin = AuthContextHolder.getAuthUserPin();

        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();

        List<Task> todoList = taskService.createTaskQuery().taskAssignee(userpin).active().orderByTaskCreateTime()
                .desc().list();
        for (Task task : todoList) {
            Map<String, Object> singleTask = packageTaskInfo(task);
            singleTask.put("needClaim", false);
            tasks.add(singleTask);
        }

        // 等待签收的任务
        List<Task> toClaimList = taskService.createTaskQuery().taskCandidateUser(userpin).active()
                .orderByTaskCreateTime().desc().list();
        for (Task task : toClaimList) {
            Map<String, Object> singleTask = packageTaskInfo(task);
            singleTask.put("needClaim", true);
            tasks.add(singleTask);
        }

        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("tasks", tasks);

        return new DefaultHttpHeaders("list").disableCaching();
    }

    public HttpHeaders show() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String taskId = request.getParameter("id");
        Assert.notNull(taskId);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        request.setAttribute("task", task);

        TaskFormDataImpl taskFormData = (TaskFormDataImpl) formService.getTaskFormData(taskId);
        String formKey = taskFormData.getFormKey();
        if (StringUtils.isBlank(formKey)) {
            formKey = DYNA_FORM_KEY + "?id=" + taskId;
        } else {
            formKey = formKey + (formKey.indexOf("?") > -1 ? "&" : "?") + "taskId=" + taskId;
        }
        request.setAttribute("formKey", formKey);
        return new DefaultHttpHeaders("show").disableCaching();
    }

    public HttpHeaders dynaForm() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String taskId = request.getParameter("id");
        TaskFormDataImpl taskFormData = (TaskFormDataImpl) formService.getTaskFormData(taskId);
        request.setAttribute("taskFormData", taskFormData);
        return new DefaultHttpHeaders("form").disableCaching();
    }

    public HttpHeaders claim() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String userpin = AuthContextHolder.getAuthUserPin();
        String taskId = request.getParameter("id");
        taskService.claim(taskId, userpin);
        model = OperationResult.buildSuccessResult("任务签收成功");
        return new DefaultHttpHeaders().disableCaching();
    }

    public HttpHeaders complete() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String userpin = AuthContextHolder.getAuthUserPin();
        String taskId = request.getParameter("id");

        Map<String, String> formProperties = new HashMap<String, String>();
        // 从request中读取参数然后转换
        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
        for (Entry<String, String[]> entry : entrySet) {
            String key = entry.getKey();
            // fp_的意思是form paremeter
            if (StringUtils.defaultString(key).startsWith("fp_")) {
                formProperties.put(key.split("_")[1], entry.getValue()[0]);
            }
        }
        identityService.setAuthenticatedUserId(userpin);
        formService.submitTaskFormData(taskId, formProperties);
        model = OperationResult.buildSuccessResult("任务处理成功");
        return new DefaultHttpHeaders().disableCaching();
    }

    public Map<String, String> getBackActivities() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String taskId = request.getParameter("id");
        List<ActivityImpl> activityImpls = activitiService.findBackActivities(taskId);
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        for (ActivityImpl activityImpl : activityImpls) {
            dataMap.put(activityImpl.getId(), ObjectUtils.toString(activityImpl.getProperty("name")));
        }
        return dataMap;
    }

    public HttpHeaders backActivity() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String taskId = request.getParameter("id");
        String activityId = request.getParameter("activityId");
        activitiService.backActivity(taskId, activityId);
        model = OperationResult.buildSuccessResult("流程实例跳转请求处理成功");
        return new DefaultHttpHeaders().disableCaching();
    }
}
