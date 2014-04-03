package lab.s2jh.ctx.service.test;

import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.ctx.DynamicConfigService;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DynamicConfigServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Test
    public void getString() {
        String dev = dynamicConfigService.getString("dev.mode", "NOT");
        Assert.assertTrue(dev.equalsIgnoreCase("true") || dev.equalsIgnoreCase("false"));
    }
}
