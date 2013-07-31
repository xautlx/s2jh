package lab.s2jh.biz.xs.service.test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lab.s2jh.biz.xs.entity.XsJbxx;
import lab.s2jh.biz.xs.service.XsJbxxService;
import lab.s2jh.biz.xx.entity.XxBj;
import lab.s2jh.biz.xx.entity.XxJcxx;
import lab.s2jh.biz.xx.service.XxBjService;
import lab.s2jh.biz.xx.service.XxJcxxService;
import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.test.TestObjectUtils;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(locations = { "classpath:/context/spring*.xml" })
public class XsJbxxServiceTester extends SpringTransactionalTestCase {

    @Autowired
    private XxBjService xxBjService;
    
    @Autowired
    private XxJcxxService xxJcxxService;
    
    @Autowired
    private XsJbxxService xsJbxxService;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Test
    public void save(){
        
        XxJcxx xxJcxx=xxJcxxService.save(TestObjectUtils.buildMockObject(XxJcxx.class));
        
        XxBj xxBj=TestObjectUtils.buildMockObject(XxBj.class);
        xxBj.setXxdm(xxJcxx.getXxdm());
        xxBjService.save(xxBj);
        
        XsJbxx xsJbxx = TestObjectUtils.buildMockObject(XsJbxx.class);
        xsJbxx.setXxBj(xxBj);
        xsJbxx.setXxdm(xxJcxx.getXxdm());
        xsJbxx.setXm("A");
        xsJbxxService.save(xsJbxx);
        entityManager.flush();
        
        xsJbxx.setXm("B");
        xsJbxxService.save(xsJbxx);
        entityManager.flush();
    }
}
