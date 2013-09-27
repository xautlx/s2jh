package lab.s2jh.schedule.service.test;

import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.schedule.service.JobBeanCfgService;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath:/service/spring-schedule.xml" })
public class JobBeanCfgServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private JobBeanCfgService jobBeanCfgService;

    @Test
    public void findAllTriggers() {
        Assert.assertTrue("Don't find any Quartz trigger.",jobBeanCfgService.findAllTriggers().size() >= 1);
    }

}
