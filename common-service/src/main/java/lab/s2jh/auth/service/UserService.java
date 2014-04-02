package lab.s2jh.auth.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lab.s2jh.auth.dao.PrivilegeDao;
import lab.s2jh.auth.dao.RoleDao;
import lab.s2jh.auth.dao.UserDao;
import lab.s2jh.auth.dao.UserLogonLogDao;
import lab.s2jh.auth.dao.UserOauthDao;
import lab.s2jh.auth.dao.UserR2RoleDao;
import lab.s2jh.auth.entity.Privilege;
import lab.s2jh.auth.entity.Role;
import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.entity.UserLogonLog;
import lab.s2jh.auth.entity.UserOauth;
import lab.s2jh.auth.entity.UserR2Role;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.service.BaseService;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService extends BaseService<User, Long> {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserOauthDao userOauthDao;

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
    protected BaseDao<User, Long> getEntityDao() {
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

    public User findBySigninid(String signinid) {
        return findByProperty("signinid", signinid);
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

    public void updateRelatedRoleR2s(Long id, String[] roleIds) {
        updateRelatedR2s(id, roleIds, "userR2Roles", "role");
    }

    @Transactional(readOnly = true)
    public List<Privilege> findRelatedPrivilegesForUser(User user) {
        return privilegeDao.findPrivilegesForUser(user);
    }

    @Async
    public void userLogonLog(UserLogonLog userLogonLog) {
        if (userLogonLogDao.findByHttpSessionId(userLogonLog.getHttpSessionId()) != null) {
            return;
        }
        User user = userDao.findByUid(userLogonLog.getUserid());
        if (user == null) {
            return;
        }
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

    public User findByOauthUser(String username) {
        UserOauth userOauth = userOauthDao.findByUsername(username);
        if (userOauth == null) {
            return null;
        } else {
            return userOauth.getUser();
        }
    }

    public Long findUserCount() {
        return userDao.findUserCount();
    }

    /**
     * 加载用户权限数据对象
     * @param username
     * @return
     */
    public UserDetails loadUserDetails(String username) {
        logger.debug("Loading user details for: {}", username);
        User user = findByProperty("signinid", username);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }

        boolean enabled = user.getEnabled() == null ? true : user.getEnabled();
        boolean accountNonLocked = user.getAccountNonLocked() == null ? true : user.getAccountNonLocked();
        Date now = new Date();
        boolean credentialsNonExpired = user.getCredentialsExpireTime() == null ? true : user
                .getCredentialsExpireTime().after(now);
        boolean accountNonExpired = user.getAccountExpireTime() == null ? true : user.getAccountExpireTime().after(now);

        if (!enabled) {
            throw new DisabledException("User '" + username + "' disabled");
        }
        if (!credentialsNonExpired) {
            throw new CredentialsExpiredException("User '" + username + "' credentials expired");
        }
        if (!accountNonLocked) {
            throw new LockedException("User '" + username + "' account locked");
        }
        if (!accountNonExpired) {
            throw new AccountExpiredException("User '" + username + "' account expired");
        }

        Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();
        Iterable<UserR2Role> r2s = userR2RoleDao.findEnabledRolesForUser(user);
        for (UserR2Role userR2Role : r2s) {
            String roleCode = userR2Role.getRole().getCode();
            dbAuthsSet.add(new SimpleGrantedAuthority(roleCode));
        }
        dbAuthsSet.add(new SimpleGrantedAuthority(Role.ROLE_ANONYMOUSLY_CODE));

        if (logger.isDebugEnabled()) {
            logger.debug("User role list for: {}", username);
            for (GrantedAuthority ga : dbAuthsSet) {
                logger.debug(" - " + ga.getAuthority());
            }
        }

        AuthUserDetails authUserDetails = new AuthUserDetails(username, user.getPassword(), enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, dbAuthsSet);
        authUserDetails.setUid(user.getUid());
        authUserDetails.setAclCode(user.getAclCode());
        authUserDetails.setAclType(user.getAclType());
        authUserDetails.setEmail(user.getEmail());

        if (aclService != null) {
            authUserDetails.setAclCodePrefixs(aclService.getStatAclCodePrefixs(user.getAclCode()));
        }

        // 处理用户拥有的权限代码集合
        Set<String> privilegeCodeSet = new HashSet<String>();
        List<Privilege> privileges = privilegeDao.findPrivilegesForUser(user);
        if (logger.isDebugEnabled()) {
            logger.debug("User privilege list for: {}", username);
            for (Privilege privilege : privileges) {
                logger.debug(" - {} : {}", privilege.getCode(), privilege.getUrl());
            }
        }
        for (Privilege privilege : privileges) {
            privilegeCodeSet.add(privilege.getCode().trim());
        }
        authUserDetails.setPrivilegeCodes(privilegeCodeSet);

        return authUserDetails;
    }
}
