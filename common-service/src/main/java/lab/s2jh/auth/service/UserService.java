package lab.s2jh.auth.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import lab.s2jh.auth.dao.PrivilegeDao;
import lab.s2jh.auth.dao.RoleDao;
import lab.s2jh.auth.dao.UserDao;
import lab.s2jh.auth.dao.UserLogonLogDao;
import lab.s2jh.auth.dao.UserR2RoleDao;
import lab.s2jh.auth.entity.Privilege;
import lab.s2jh.auth.entity.Role;
import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.entity.UserLogonLog;
import lab.s2jh.auth.entity.UserR2Role;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.service.R2OperationEnum;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class UserService extends BaseService<User, String> {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PrivilegeDao privilegeDao;

    @Autowired
    private UserR2RoleDao userR2RoleDao;

    @Autowired
    private UserLogonLogDao userLogonLogDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private AclService aclService;

    @Override
    protected BaseDao<User, String> getEntityDao() {
        return userDao;
    }

    @Override
    protected void preInsert(User entity) {
        super.preInsert(entity);
        if (aclService != null) {
            entity.setAclType(aclService.aclCodeToType(entity.getAclCode()));
        }
        entity.setSignupTime(new Date());
    }

    @Override
    protected void preUpdate(User entity) {
        if (aclService != null) {
            entity.setAclType(aclService.aclCodeToType(entity.getAclCode()));
        }
        super.preUpdate(entity);
    }

    public User findByAclCodeAndSigninid(String aclCode, String signid) {
        if (StringUtils.isBlank(aclCode)) {
            List<User> users = userDao.findBySigninid(signid);
            if (CollectionUtils.isEmpty(users)) {
                return null;
            }
            Assert.isTrue(users.size() == 1);
            return users.get(0);
        }
        return userDao.findByAclCodeAndSigninid(aclCode, signid);
    }

    public User findByUid(String uid) {
        return userDao.findByUid(uid);
    }

    public long findCount() {
        return userDao.count();
    }

    /**
     * 用户注册
     * 
     * @param rawPassword
     *            原始密码
     * @param user
     *            用户数据对象
     * @return
     */
    public User save(User user, String rawPassword) {
        if (user.isNew()) {
            user.setUid(RandomStringUtils.randomNumeric(10));
        }
        if (StringUtils.isNotBlank(rawPassword)) {
            user.setPassword(encodeUserPasswd(user, rawPassword));
        }
        return this.save(user);
    }

    public String encodeUserPasswd(User user, String rawPassword) {
        return passwordEncoder.encodePassword(rawPassword, user.getUid());
    }

    /**
     * 初始化系统用户设置
     * @param user
     * @param rawPassword
     * @return
     */
    public User initSetupUser(User user, String rawPassword) {
        long count = findCount();
        Assert.isTrue(count == 0);
        user.setInitSetupUser(true);
        save(user, rawPassword);
        Role role = roleDao.findByCode(Role.ROLE_ADMIN_CODE);
        if (role == null) {
            role = new Role();
            role.setCode(Role.ROLE_ADMIN_CODE);
            role.setTitle("预置系统管理员");
            role.setDescription("预置角色,不可删除");
            roleDao.save(role);
        }
        UserR2Role r2 = new UserR2Role();
        r2.setUser(user);
        r2.setRole(role);
        userR2RoleDao.save(r2);
        return user;
    }

    @Transactional(readOnly = true)
    public List<UserR2Role> findRelatedUserR2RolesForUser(String userId) {
        return userR2RoleDao.findByUser_Id(userId);
    }

    public void updateRelatedRoleR2s(String id, Collection<String> roleIds, R2OperationEnum op) {
        updateRelatedR2s(id, roleIds, "userR2Roles", "role", op);
    }

    @Transactional(readOnly = true)
    public List<Privilege> findRelatedPrivilegesForUser(String userId) {
        return privilegeDao.findPrivilegesForUser(userId);
    }

    @Async
    public void userLogonLog(UserLogonLog userLogonLog) {
        User user = userDao.findByUid(userLogonLog.getUserid());
        if (user.getLogonTimes() == null) {
            user.setLogonTimes(1L);
        } else {
            user.setLogonTimes(user.getLogonTimes() + 1L);
        }
        userLogonLog.setLogonTimes(user.getLogonTimes());
        user.setLastLogonIP(userLogonLog.getRemoteAddr());
        user.setLastLogonHost(userLogonLog.getRemoteHost());
        user.setLastLogonTime(userLogonLog.getLogonTime());
        userDao.save(user);
        userLogonLogDao.save(userLogonLog);
    }
}
