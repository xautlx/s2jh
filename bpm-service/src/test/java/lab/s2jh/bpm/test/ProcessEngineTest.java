package lab.s2jh.bpm.test;

import static org.junit.Assert.assertNotNull;
import lab.s2jh.core.test.SpringTransactionalTestCase;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath*:/service/spring-bpm.xml" })
public class ProcessEngineTest extends SpringTransactionalTestCase {

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

    @Test
    public void testProcessEngines() {
        assertNotNull(repositoryService);
        assertNotNull(runtimeService);
        assertNotNull(formService);
        assertNotNull(identityService);
        assertNotNull(taskService);
        assertNotNull(historyService);
        assertNotNull(managementService);
    }

}
