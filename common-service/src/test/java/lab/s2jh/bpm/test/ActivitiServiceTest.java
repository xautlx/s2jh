package lab.s2jh.bpm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import lab.s2jh.bpm.service.ActivitiService;
import lab.s2jh.core.test.SpringTransactionalTestCase;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath*:/service/spring-bpm.xml" })
public class ActivitiServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private ActivitiService activitiService;

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

    @Before
    public void createDeployment() {
        repositoryService.createDeployment().addClasspathResource("bpm/ActivitiServiceTest.bpmn").deploy();
    }

    @Test
    public void findBackAvtivities() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("ActivitiServiceTest").singleResult();
        assertNotNull(processDefinition);

        identityService.setAuthenticatedUserId("s2jh");

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        assertNotNull(processInstance.getId());

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        logger.debug("Task Name: {}", task.getName());
        assertEquals("UserTask1", task.getName());
        taskService.complete(task.getId());

        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        logger.debug("Task Name: {}", task.getName());
        assertEquals("UserTask2", task.getName());
        List<ActivityImpl> activityImpls = activitiService.findBackActivities(task.getId());
        for (ActivityImpl activityImpl : activityImpls) {
            logger.debug("activityImpl: {}", activityImpl);
        }

        taskService.complete(task.getId());

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task t : tasks) {
            logger.debug(" - Task Name: {}", t.getName());
        }
        assertEquals(2, tasks.size());

        activityImpls = activitiService.findBackActivities(tasks.get(0).getId());
        for (ActivityImpl activityImpl : activityImpls) {
            logger.debug("activityImpl: {}", activityImpl);
        }
        assertEquals(0, activityImpls.size());

        taskService.complete(tasks.get(0).getId());
        activityImpls = activitiService.findBackActivities(tasks.get(1).getId());
        for (ActivityImpl activityImpl : activityImpls) {
            logger.debug("activityImpl: {}", activityImpl);
        }
        assertEquals(0, activityImpls.size());

        taskService.complete(tasks.get(1).getId());

        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        logger.debug("Task Name: {}", task.getName());
        assertEquals("UserTask5", task.getName());
        activityImpls = activitiService.findBackActivities(task.getId());
        for (ActivityImpl activityImpl : activityImpls) {
            logger.debug("activityImpl: {}", activityImpl);
        }
        assertEquals(2, activityImpls.size());

        taskService.complete(task.getId());

        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        logger.debug("Task Name: {}", task.getName());
        assertEquals("UserTask6", task.getName());
        activityImpls = activitiService.findBackActivities(task.getId());
        for (ActivityImpl activityImpl : activityImpls) {
            logger.debug("activityImpl: {}", activityImpl);
        }
        assertEquals(3, activityImpls.size());

        activitiService.backActivity(task.getId(), activityImpls.get(0).getId());

        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        logger.debug("Task Name: {}", task.getName());
        assertEquals("UserTask1", task.getName());

        taskService.complete(task.getId());

        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        logger.debug("Task Name: {}", task.getName());
        assertEquals("UserTask2", task.getName());

        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstance.getId()).orderByHistoricTaskInstanceStartTime().asc().list();
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            logger.debug("historicTaskInstance: {}", historicTaskInstance.getName());
        }
    }
}
