package lab.s2jh.auth.service.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lab.s2jh.auth.entity.Privilege;
import lab.s2jh.auth.service.PrivilegeService;
import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.test.TestObjectUtils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PrivilegeServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private PrivilegeService privilegeService;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void findDistinctCategory() {
        Privilege entity = TestObjectUtils.buildMockObject(Privilege.class);
        entity.setCategory("ABC");
        privilegeService.save(entity);

        Privilege entity2 = TestObjectUtils.buildMockObject(Privilege.class);
        entity2.setCategory("ABC");
        privilegeService.save(entity2);

        List<String> categories = privilegeService.findDistinctCategories();
        for (String category : categories) {
            logger.debug("category: {}", category);
        }
        Assert.assertTrue(categories.size() >= 1);

        logger.debug("Testing data cache...");
        privilegeService.findDistinctCategories();
    }

    @Test
    public void findAllCached() {
        Privilege entity = TestObjectUtils.buildMockObject(Privilege.class);
        entity.setCategory("ABC");
        privilegeService.save(entity);
        em.flush();
        logger.debug("Fisrt load data...");
        privilegeService.findAllCached();
        em.flush();
        logger.debug("Load cached data...");
        privilegeService.findAllCached();
        em.flush();
    }
}
