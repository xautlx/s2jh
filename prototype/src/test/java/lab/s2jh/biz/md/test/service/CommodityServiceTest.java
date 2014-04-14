package lab.s2jh.biz.md.test.service;

import java.util.List;

import lab.s2jh.biz.md.entity.Commodity;
import lab.s2jh.biz.md.service.CommodityService;
import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.test.TestObjectUtils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CommodityServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private CommodityService commodityService;

    @Test
    public void findByPage() {
        //Insert mock entity
        Commodity entity = TestObjectUtils.buildMockObject(Commodity.class);
        commodityService.save(entity);
        Assert.assertTrue(entity.getId() != null);

        //JPA/Hibernate query validation
        List<Commodity> items = commodityService.findAll(entity.getId());
        Assert.assertTrue(items.size() >= 1);
    }
}