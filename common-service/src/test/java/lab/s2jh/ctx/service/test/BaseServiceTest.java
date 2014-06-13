package lab.s2jh.ctx.service.test;

import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.entity.UserLogonLog;
import lab.s2jh.auth.service.UserLogonLogService;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.test.TestObjectUtils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

public class BaseServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private UserService userService;

    @Autowired
    private UserLogonLogService userLogonLogService;

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

    @Test
    public void group() {

        UserLogonLog entity1 = TestObjectUtils.buildMockObject(UserLogonLog.class);
        entity1.setUsername("abc");
        userLogonLogService.save(entity1);

        UserLogonLog entity2 = TestObjectUtils.buildMockObject(UserLogonLog.class);
        entity2.setUsername("abc");
        userLogonLogService.save(entity2);

        UserLogonLog entity3 = TestObjectUtils.buildMockObject(UserLogonLog.class);
        entity3.setUsername("xyz");
        userLogonLogService.save(entity3);

        Map<String, Expression<?>> aliasMapping = Maps.newHashMap();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Session session = entityManager.unwrap(Session.class);
        //session.createSQLQuery("SET ANSI_WARNINGS OFF;SET ARITHABORT OFF;").executeUpdate();
        //entityManager.createNativeQuery("SET ANSI_WARNINGS OFF;SET ARITHABORT OFF;").executeUpdate();

        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<UserLogonLog> root = criteriaQuery.from(UserLogonLog.class);
        Expression<String> usernameExpr = root.get("username");
        aliasMapping(aliasMapping, "username", usernameExpr);
        Expression<Long> countExpr = criteriaBuilder.count(root.get("username"));
        aliasMapping(aliasMapping, "total", countExpr);

        Expression logonTimeLengthExpr = criteriaBuilder.selectCase().when(criteriaBuilder.le(countExpr, 1), -1)
                .otherwise(countExpr);

        CriteriaQuery<Tuple> select = criteriaQuery.multiselect(countExpr, usernameExpr, logonTimeLengthExpr);
        select.groupBy(usernameExpr);
        select.orderBy(criteriaBuilder.asc(aliasMapping.get("total")));

        TypedQuery<Tuple> query = entityManager.createQuery(select);
        List<Tuple> tuples = query.getResultList();
        for (Tuple tuple : tuples) {
            Map<String, Object> row = Maps.newHashMap();
            for (String alias : aliasMapping.keySet()) {
                row.put(alias, tuple.get(alias));
            }
            logger.debug("Tuple Row: {}", row);
        }
    }

    private void aliasMapping(Map<String, Expression<?>> aliasMapping, String alias, Expression<?> expr) {
        aliasMapping.put(alias, (Expression<?>) expr.alias(alias));
    }

    @Test
    public void findByGroupAggregate() {
        userLogonLogService.findByGroupAggregate(null, null, "username",
                "case(equal(count(username),0),-1,count(username))");
    }
}
