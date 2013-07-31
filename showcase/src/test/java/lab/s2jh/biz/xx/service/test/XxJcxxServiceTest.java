package lab.s2jh.biz.xx.service.test;

import lab.s2jh.biz.xx.entity.XxFzxx;
import lab.s2jh.biz.xx.entity.XxJcxx;
import lab.s2jh.biz.xx.service.XxJcxxService;
import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.test.TestObjectUtils;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(locations = { "classpath:/context/spring*.xml" })
public class XxJcxxServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private XxJcxxService xxJcxxService;

    @Test
    public void save() {
        XxJcxx xxJcxx = TestObjectUtils.buildMockObject(XxJcxx.class);
        xxJcxxService.save(xxJcxx);
        
        XxFzxx xxFzxx = TestObjectUtils.buildMockObject(XxFzxx.class);
        xxFzxx.setXxdm(xxJcxx.getXxdm());
        xxJcxxService.save(xxJcxx);
        
        entityManager.flush();
    }

    @Test
    public void findOne() {
        xxJcxxService.findOne("ABC");
    }
}