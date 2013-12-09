package lab.s2jh.bpm.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.util.DateUtils;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.RestActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class BpmTaskController extends RestActionSupport {

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

    private Map<String, Object> packageTaskInfo(Task task) {

        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();

        Map<String, Object> singleTask = new HashMap<String, Object>();
        singleTask.put("id", task.getId());
        singleTask.put("name", task.getName());
        singleTask.put("createTime", DateUtils.formatTime(task.getCreateTime()));
        singleTask.put("pdname", processDefinition.getName());
        singleTask.put("pdversion", processDefinition.getVersion());
        singleTask.put("pid", task.getProcessInstanceId());

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

        List<Task> todoList = taskService.createTaskQuery().taskAssignee(userpin).active().list();
        for (Task task : todoList) {
            Map<String, Object> singleTask = packageTaskInfo(task);
            singleTask.put("needClaim", false);
            tasks.add(singleTask);
        }

        // 等待签收的任务
        List<Task> toClaimList = taskService.createTaskQuery().taskCandidateUser(userpin).active().list();
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
        TaskFormDataImpl taskFormData = (TaskFormDataImpl) formService.getTaskFormData(taskId);
        String formKey = taskFormData.getFormKey();
        if (StringUtils.isBlank(formKey)) {
            formKey = DYNA_FORM_KEY;
        }
        request.setAttribute("formKey", formKey);
        return new DefaultHttpHeaders("show").disableCaching();
    }

    public HttpHeaders dynaForm() {
        return new DefaultHttpHeaders("form").disableCaching();
    }
}
