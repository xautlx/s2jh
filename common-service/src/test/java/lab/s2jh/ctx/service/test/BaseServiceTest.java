package lab.s2jh.ctx.service.test;

import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.test.TestObjectUtils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private UserService userService;

    @Test
    public void testGroupPropertyFilter() {
        User user = null;

        user = TestObjectUtils.buildMockObject(User.class);
        user.setEmail("1@abc.com");
        userService.save(user);

        user = TestObjectUtils.buildMockObject(User.class);
        user.setEmail("2@abc.com");
        userService.save(user);

        user = TestObjectUtils.buildMockObject(User.class);
        user.setEmail("3@abc.com");
        userService.save(user);

        GroupPropertyFilter groupPropertyFilter = GroupPropertyFilter.buildDefaultOrGroupFilter();
        groupPropertyFilter.append(new PropertyFilter(MatchType.EQ, "email", "1@abc.com"));
        groupPropertyFilter.append(new PropertyFilter(MatchType.EQ, "email", "2@abc.com"));
        Assert.assertTrue(userService.findByFilters(groupPropertyFilter).size() == 2);

        GroupPropertyFilter groupPropertyFilter2 = GroupPropertyFilter.buildDefaultAndGroupFilter();
        groupPropertyFilter2.append(new PropertyFilter(MatchType.EQ, "email", "1@abc.com"));
        groupPropertyFilter2.append(new PropertyFilter(MatchType.EQ, "email", "2@abc.com"));
        Assert.assertTrue(userService.findByFilters(groupPropertyFilter2).size() == 0);
        
        GroupPropertyFilter groupPropertyFilter3 = GroupPropertyFilter.buildDefaultOrGroupFilter();
        groupPropertyFilter3.append(new PropertyFilter(MatchType.EQ, "email", "1@abc.com"));
        groupPropertyFilter3.append(new PropertyFilter(MatchType.EQ, "email", "2@abc.com"));
        groupPropertyFilter3.forceAnd(new PropertyFilter(MatchType.EQ, "email", "4@abc.com"));
        Assert.assertTrue(userService.findByFilters(groupPropertyFilter3).size() == 0);
    }
}
