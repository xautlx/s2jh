package lab.s2jh.biz.demo.test.service;

import java.util.List;

import lab.s2jh.biz.demo.entity.Demo;
import lab.s2jh.biz.demo.service.DemoService;
import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.test.TestObjectUtils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;


public class DemoServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private DemoService demoService;

    @Test
    public void findByPage() {
        //Insert mock entity
        Demo entity = TestObjectUtils.buildMockObject(Demo.class);
        demoService.save(entity);
        Assert.assertTrue(entity.getId() != null);

        //JPA/Hibernate query validation
        List<Demo> items = demoService.findAll(Sets.newHashSet(entity.getId()));
        Assert.assertTrue(items.size() >= 1);
    }
}