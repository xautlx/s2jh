package lab.s2jh.sys.service.test;

import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.test.TestObjectUtils;
import lab.s2jh.sys.entity.Menu;
import lab.s2jh.sys.service.MenuService;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.CollectionUtils;

public class MenuServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private MenuService menuService;

    @Test
    public void findRoot() {
        Menu entity = TestObjectUtils.buildMockObject(Menu.class);
        entity.setParent(null);
        menuService.save(entity);
        Iterable<Menu> roots = menuService.findRoots();
        for (Menu item : roots) {
            logger.debug("Item: {}", item);
        }

        logger.debug("Testing data cache...");
        menuService.findRoots();

        logger.debug("Testing data cache...");
        menuService.findRoots();
    }

    @Test
    public void findByPage() {

        Menu entity = TestObjectUtils.buildMockObject(Menu.class);
        entity.setTitle("ABC123");
        menuService.save(entity);

        Menu entity2 = TestObjectUtils.buildMockObject(Menu.class);
        entity2.setTitle("ABC456");
        menuService.save(entity2);

        GroupPropertyFilter groupPropertyFilter = new GroupPropertyFilter();
        groupPropertyFilter.and(new PropertyFilter(MatchType.EQ, "title", "ABC123"));
        Pageable pageable = new PageRequest(0, 10, new Sort(Direction.DESC, "id"));
        Page<Menu> items = menuService.findByPage(groupPropertyFilter, pageable);
        Assert.assertTrue(items.getContent().size() == 1);

        groupPropertyFilter = new GroupPropertyFilter();
        groupPropertyFilter.and(new PropertyFilter(MatchType.EQ, "title", "ABC789"));
        pageable = new PageRequest(0, 10, new Sort(Direction.DESC, "id"));
        items = menuService.findByPage(groupPropertyFilter, pageable);
        Assert.assertTrue(CollectionUtils.isEmpty(items.getContent()));

        groupPropertyFilter = new GroupPropertyFilter();
        groupPropertyFilter.and(new PropertyFilter(MatchType.CN, "title", "ABC"));
        pageable = new PageRequest(0, 10, new Sort(Direction.DESC, "id"));
        items = menuService.findByPage(groupPropertyFilter, pageable);
        Assert.assertTrue(items.getContent().size() == 2);
    }

    @Test
    public void findEntityRevisions() {
        Menu entity = TestObjectUtils.buildMockObject(Menu.class);
        entity.setTitle("AAA");
        menuService.save(entity);
        entityManager.flush();

        entity.setTitle("BBB");
        menuService.save(entity);
        entityManager.flush();

        menuService.findEntityRevisions(entity.getId(), null, null);
    }
}
