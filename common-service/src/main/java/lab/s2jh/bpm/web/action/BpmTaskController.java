package lab.s2jh.bpm.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.bpm.service.ActivitiService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.web.SimpleController;
import lab.s2jh.core.web.annotation.SecurityControllIgnore;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.ctx.DynamicConfigService;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;

public class BpmTaskController extends SimpleController {

    private final static String DYNA_FORM_KEY = "/bpm/bpm-task!dynaForm";

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

    @Autowired
    protected UserService userService;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    private Map<String, Object> packageTaskInfo(Task task) {

        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).singleResult();
        Map<String, Object> variables = taskService.getVariables(task.getId());
        Map<String, Object> singleTask = new HashMap<String, Object>();
        singleTask.put("id", task.getId());
        singleTask.put("name", task.getName());
        singleTask.put("createTime", task.getCreateTime());
        singleTask.put("pdname", processDefinition.getName());
        singleTask.put("pdversion", processDefinition.getVersion());
        singleTask.put("pid", task.getProcessInstanceId());
        singleTask.put("bizKey", processInstance.getBusinessKey());
        singleTask.put("initiator", variables.get(ActivitiService.BPM_INITIATOR_VAR_NAME));
        return singleTask;
    }

    @MetaData(value = "用户待办任务列表")
    @SecurityControllIgnore
    public HttpHeaders userTasks() {
        // 已经签收的任务
        String userpin = AuthContextHolder.getAuthUserPin();

        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();

        List<Task> todoList = taskService.createTaskQuery().taskAssignee(userpin).active().orderByTaskCreateTime()
                .desc().list();
        for (Task task : todoList) {
            Map<String, Object> singleTask = packageTaskInfo(task);
            singleTask.put("candidate", false);
            tasks.add(singleTask);
        }

        // 等待签收的任务
        List<Task> toClaimList = taskService.createTaskQuery().taskCandidateUser(userpin).active()
                .orderByTaskCreateTime().desc().list();
        for (Task task : toClaimList) {
            Map<String, Object> singleTask = packageTaskInfo(task);
            singleTask.put("candidate", true);
            tasks.add(singleTask);
        }

        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("tasks", tasks);

        return new DefaultHttpHeaders("list").disableCaching();
    }

    /**
     * 基于任务参数查询Task实例，其中追加当前登录用户条件确保不会出现非法数据访问
     * @param taskId
     * @param candidate
     * @return
     */
    private Task getUserTaskByRequest() {
        String userpin = AuthContextHolder.getAuthUserPin();
        HttpServletRequest request = ServletActionContext.getRequest();
        String taskId = request.getParameter("taskId");
        Assert.notNull(taskId);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task.getAssignee() == null) {
            task = taskService.createTaskQuery().taskId(taskId).taskCandidateUser(userpin).singleResult();
            Assert.notNull(task, "BPM Task access denied");
        } else {
            Assert.isTrue(task.getAssignee().equals(userpin), "Task assignee not match");
        }
        return task;
    }

    @MetaData(value = "任务显示")
    public HttpHeaders show() {
        HttpServletRequest request = ServletActionContext.getRequest();
        Task task = getUserTaskByRequest();
        request.setAttribute("task", task);

        TaskFormDataImpl taskFormData = (TaskFormDataImpl) formService.getTaskFormData(task.getId());
        String formKey = taskFormData.getFormKey();
        if (StringUtils.isBlank(formKey)) {
            formKey = DYNA_FORM_KEY + "?id=" + task.getId();
        } else {
            formKey = formKey + (formKey.indexOf("?") > -1 ? "&" : "?") + "taskId=" + task.getId();
        }
        request.setAttribute("formKey", formKey);
        return new DefaultHttpHeaders("show").disableCaching();
    }

    @MetaData(value = "任务变量清单显示")
    public HttpHeaders variables() {
        HttpServletRequest request = ServletActionContext.getRequest();
        Task task = getUserTaskByRequest();
        Map<String, Object> variables = taskService.getVariables(task.getId());
        request.setAttribute("variables", variables);
        return new DefaultHttpHeaders("variables").disableCaching();
    }

    public HttpHeaders dynaForm() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String taskId = request.getParameter("id");
        TaskFormDataImpl taskFormData = (TaskFormDataImpl) formService.getTaskFormData(taskId);
        request.setAttribute("taskFormData", taskFormData);
        return new DefaultHttpHeaders("form").disableCaching();
    }

    @MetaData(value = "任务签收")
    public HttpHeaders claim() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String userpin = AuthContextHolder.getAuthUserPin();
        String taskId = request.getParameter("id");
        taskService.claim(taskId, userpin);
        model = OperationResult.buildSuccessResult("任务签收成功", userpin);
        return new DefaultHttpHeaders().disableCaching();
    }

    @MetaData(value = "任务转办")
    public HttpHeaders trasfer() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String taskId = request.getParameter("id");
        String assignee = request.getParameter("assignee");
        User user = userService.findBySigninid(assignee);
        if (user == null) {
            model = OperationResult.buildFailureResult("未找到匹配登录账号：" + assignee);
        } else {
            taskService.setAssignee(taskId, assignee);
            model = OperationResult.buildSuccessResult("任务转办操作成功");
        }
        return new DefaultHttpHeaders().disableCaching();
    }

    public HttpHeaders complete() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String userpin = AuthContextHolder.getAuthUserPin();
        String taskId = request.getParameter("id");

        Map<String, String> formProperties = new HashMap<String, String>();
        // 从request中读取参数然后转换
        @SuppressWarnings("unchecked")
        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
        for (Entry<String, String[]> entry : entrySet) {
            String key = entry.getKey();
            // fp_的意思是form paremeter
            if (StringUtils.defaultString(key).startsWith("fp_")) {
                formProperties.put(key.split("_")[1], entry.getValue()[0]);
            }
        }
        activitiService.submitTaskFormData(taskId, formProperties);
        model = OperationResult.buildSuccessResult("任务处理成功");
        return new DefaultHttpHeaders().disableCaching();
    }

    @MetaData(value = "任务回退表单显示")
    public HttpHeaders backActivity() {
        HttpServletRequest request = ServletActionContext.getRequest();
        Task task = getUserTaskByRequest();
        List<ActivityImpl> activityImpls = activitiService.findBackActivities(task.getId());
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        for (ActivityImpl activityImpl : activityImpls) {
            dataMap.put(activityImpl.getId(), ObjectUtils.toString(activityImpl.getProperty("name")));
        }
        request.setAttribute("task", task);
        request.setAttribute("backActivities", dataMap);
        return new DefaultHttpHeaders("backActivity").disableCaching();
    }

    @MetaData(value = "任务回退处理")
    public HttpHeaders doBackActivity() {
        Assert.isTrue(isProcessBackSupport(), "任务回退功能不可用");
        HttpServletRequest request = ServletActionContext.getRequest();
        Task task = getUserTaskByRequest();
        String activityId = request.getParameter("activityId");
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("backActivityExplain", request.getParameter("backActivityExplain"));
        activitiService.backActivity(task.getId(), activityId, variables);
        model = OperationResult.buildSuccessResult("流程实例跳转请求处理成功");
        return new DefaultHttpHeaders().disableCaching();
    }

    /**
     * 工作流处理的自由回退功能支持控制
     * 如果流程流转过程存在业务数据交互处理，自由回退功能很可能导致数据重复处理或不一致的情况发生
     * 因此除非流程和业务结合处理除非经过仔细的设计实现，建议关闭自由回退功能或有管理员临时干预控制
     * 可选值说明：disabled=全局关闭; enable=全局启用; admin=只有ROLE_ADMIN角色用户才有功能权限
     * @return
     */
    public boolean isProcessBackSupport() {
        String back = dynamicConfigService.getString("cfg.bpm.process.back.support", "admin");
        if (back.equalsIgnoreCase("enable")) {
            return true;
        } else if (back.equalsIgnoreCase("admin")) {
            return AuthContextHolder.isAdminUser();
        }
        return false;
    }
}
