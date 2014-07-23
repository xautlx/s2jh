package lab.s2jh.bpm.test;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lab.s2jh.core.test.SpringTransactionalTestCase;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath*:/service/spring-bpm.xml" })
public class ProcessTestS2jhBpmUt extends SpringTransactionalTestCase {

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

    @Test
    public void startProcess() throws Exception {
        repositoryService.createDeployment().addClasspathResource("bpm/s2jh_bpm_ut.bpmn").deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("s2jh_bpm_ut").singleResult();
        assertNotNull(processDefinition);

        identityService.setAuthenticatedUserId("s2jh");
        
        {
            Map<String, String> variableMap = new HashMap<String, String>();
            variableMap.put("submitToAudit", "true");
            ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), variableMap);
            assertNotNull(processInstance.getId());

            List<Task> tasks = taskService.createTaskQuery().list();
            for (Task task : tasks) {
                logger.debug("Task: {}", task);
            }
        }

        {
            Map<String, String> variableMap = new HashMap<String, String>();
            variableMap.put("submitToAudit", "false");
            ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), variableMap);
            assertNotNull(processInstance.getId());

            List<Task> tasks = taskService.createTaskQuery().list();
            for (Task task : tasks) {
                logger.debug("Task: {}", task);
            }
        }
    }
}