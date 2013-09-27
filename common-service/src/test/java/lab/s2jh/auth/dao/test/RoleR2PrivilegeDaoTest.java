package lab.s2jh.auth.dao.test;

import lab.s2jh.auth.dao.RoleR2PrivilegeDao;
import lab.s2jh.core.test.SpringTransactionalTestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleR2PrivilegeDaoTest extends SpringTransactionalTestCase {
    @Autowired
    private RoleR2PrivilegeDao roleR2PrivilegeDao;

    @Test
    public void findEnabledExcludeRole() {
        roleR2PrivilegeDao.findEnabledExcludeRole("MOCKID");
    }
}
