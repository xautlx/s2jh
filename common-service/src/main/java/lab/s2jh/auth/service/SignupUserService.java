package lab.s2jh.auth.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lab.s2jh.auth.dao.SignupUserDao;
import lab.s2jh.auth.dao.UserDao;
import lab.s2jh.auth.dao.UserR2RoleDao;
import lab.s2jh.auth.entity.Role;
import lab.s2jh.auth.entity.SignupUser;
import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.entity.UserR2Role;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class SignupUserService extends BaseService<SignupUser, String> {

    @Autowired
    private SignupUserDao signupUserDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserR2RoleDao userR2RoleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected BaseDao<SignupUser, String> getEntityDao() {
        return signupUserDao;
    }

    @Override
    public SignupUser save(SignupUser entity) {
        if (entity.isNew()) {
            entity.setUid(UUID.randomUUID().toString());
            entity.setSignupTime(new Date());
            String encodePasswd = passwordEncoder.encodePassword(entity.getPassword(), entity.getUid());
            entity.setPassword(encodePasswd);
        }
        return super.save(entity);
    }

    public void audit(SignupUser entity, List<Role> roles) {
        Assert.isTrue(entity.getAuditTime() == null);
        entity.setAuditTime(new Date());
        signupUserDao.save(entity);

        User user = new User();
        user.setAclCode(entity.getAclCode());
        user.setUid(entity.getUid());
        user.setSigninid(entity.getSigninid());
        user.setSignupTime(entity.getSignupTime());
        user.setPassword(entity.getPassword());
        userDao.save(user);

        if (CollectionUtils.isNotEmpty(roles)) {
            for (Role role : roles) {
                UserR2Role r2 = new UserR2Role();
                r2.setUser(user);
                r2.setRole(role);
                userR2RoleDao.save(r2);
            }
        }
    }
}
