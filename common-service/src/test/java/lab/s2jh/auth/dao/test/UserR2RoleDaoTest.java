package lab.s2jh.auth.dao.test;

import lab.s2jh.auth.dao.UserR2RoleDao;
import lab.s2jh.core.test.SpringTransactionalTestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath:/context/spring*.xml" })
public class UserR2RoleDaoTest extends SpringTransactionalTestCase {
    @Autowired
    private UserR2RoleDao userR2RoleDao;

    @Test
    public void findByUser_Id() {
        userR2RoleDao.findByUser_Id("MOCKID");
    }
}
