package lab.s2jh.sys.service.test;

import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.test.TestObjectUtils;
import lab.s2jh.sys.entity.PubPost;
import lab.s2jh.sys.service.PubPostReadService;
import lab.s2jh.sys.service.PubPostService;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

public class PubPostReadServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private PubPostReadService pubPostReadService;

    @Autowired
    private PubPostService pubPostService;

    @Autowired
    private UserService userService;

    @Test
    public void findReaded() {
        PubPost entity = TestObjectUtils.buildMockObject(PubPost.class);
        pubPostService.save(entity);

        User user = TestObjectUtils.buildMockObject(User.class);
        //以下特点属性设置为了通过注解验证
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@s2jh.com");
        user.setSigninid(RandomStringUtils.randomAlphabetic(10));
        userService.save(user);

        pubPostReadService.findReaded(user, Lists.newArrayList(entity));
    }
}