package lab.s2jh.biz.xx.dao.test;

import lab.s2jh.biz.xx.dao.XxBjDao;
import lab.s2jh.biz.xx.entity.XxBj;
import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.test.TestObjectUtils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class XxBjDaoTest extends SpringTransactionalTestCase {

    @Autowired
    private XxBjDao xxBjDao;

    @Test
    public void findXsCount() {
        XxBj xxBj = TestObjectUtils.buildMockObject(XxBj.class);
        xxBjDao.save(xxBj);
        Long count = xxBjDao.findXsCount(xxBj.getBh());
        Assert.assertTrue(count >= 1);
    }
}