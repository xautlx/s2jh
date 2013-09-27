package lab.s2jh.sys.service.test;

import java.util.List;

import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.test.TestObjectUtils;
import lab.s2jh.sys.entity.PubPost;
import lab.s2jh.sys.service.PubPostService;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;


public class PubPostServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private PubPostService pubPostService;

    @Test
    public void findByPage() {
        //Insert mock entity
        PubPost entity = TestObjectUtils.buildMockObject(PubPost.class);
        pubPostService.save(entity);
        Assert.assertTrue(entity.getId() != null);

        //JPA/Hibernate query validation
        List<PubPost> items = pubPostService.findAll(Sets.newHashSet(entity.getId()));
        Assert.assertTrue(items.size() >= 1);
    }

    @Test
    public void findPublished() {
        //Insert mock entity
        PubPost entity = TestObjectUtils.buildMockObject(PubPost.class);
        pubPostService.save(entity);
        Assert.assertTrue(entity.getId() != null);

        logger.debug("1........");
        pubPostService.findPublished();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("2........");
        pubPostService.findPublished();
        
        entity.setHtmlTitle("ABC");
        pubPostService.save(entity);
        entityManager.flush();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("3........");
        pubPostService.findPublished();
    }
}