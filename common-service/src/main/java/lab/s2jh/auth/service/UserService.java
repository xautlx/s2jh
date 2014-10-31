package lab.s2jh.auth.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
import lab.s2jh.core.exception.ServiceException;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.ctx.DynamicConfigService;
import lab.s2jh.ctx.FreemarkerService;
import lab.s2jh.ctx.MailService;

import org.activiti.engine.IdentityService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.joda.time.DateTime;
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
import org.springframework.util.Assert;

import com.google.common.collect.Maps;

@SuppressWarnings("deprecation")
@Service
@Transactional
public class UserService extends BaseService<User, Long> {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    //采用业务逻辑视图代替自带表方式实现业务权限数据供Activiti调用
    private boolean dbIdentityUsed = false;

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

    @Autowired
    private MailService mailService;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired(required = false)
    private FreemarkerService freemarkerService;

    @Autowired(required = false)
    private AclService aclService;
    @Autowired(required = false)
    private IdentityService identityService;

    @Override
    protected BaseDao<User, Long> getEntityDao() {
        return userDao;
    }

    @Override
    protected void preInsert(User entity) {
        super.preInsert(entity);
        String email = entity.getEmail();
        if (StringUtils.isNotBlank(email) && findByProperty("email", email) != null) {
            throw new ServiceException("邮件 [" + email + "] 已被注册占用");
        }
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

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setUid(RandomStringUtils.randomNumeric(10));
        }
        super.save(user);

        // 关联处理Activiti的用户权限控制数据
        cascadeActivitiIndentityData(user);

        return user;
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
        if (StringUtils.isNotBlank(rawPassword)) {
            // 密码修改后更新密码过期时间为6个月
            user.setCredentialsExpireTime(new DateTime().plusMonths(6).toDate());
            user.setPassword(encodeUserPasswd(user, rawPassword));
        }
        return save(user);
    }

    public boolean validatePassword(String signinid, String rawPassword) {
        User user = findBySigninid(signinid);
        //用户账号不存在，直接返回密码验证失败
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(encodeUserPasswd(user, rawPassword));
    }

    public String encodeUserPasswd(User user, String rawPassword) {
        return passwordEncoder.encodePassword(rawPassword, user.getUid());
    }

    public void updateRelatedRoleR2s(Long id, String... roleIds) {
        updateRelatedR2s(id, roleIds, "userR2Roles", "role");
        // 关联处理Activiti的用户权限控制数据
        cascadeActivitiIndentityData(userDao.findOne(id), roleIds);
    }

    private void cascadeActivitiIndentityData(User user, String... roleIds) {
        if (dbIdentityUsed == false) {
            return;
        }
        if (identityService != null) {
            String userId = user.getSigninid();
            org.activiti.engine.identity.User identityUser = identityService.createUserQuery().userId(userId)
                    .singleResult();
            List<org.activiti.engine.identity.Group> activitiGroups = null;
            if (identityUser != null) {
                // 更新信息
                identityUser.setFirstName(user.getNick());
                identityUser.setLastName("");
                identityUser.setPassword(user.getPassword());
                identityUser.setEmail(user.getEmail());
                identityService.saveUser(identityUser);

                // 先删除已有的membership
                activitiGroups = identityService.createGroupQuery().groupMember(userId).list();
                for (org.activiti.engine.identity.Group group : activitiGroups) {
                    if (group.getType().equals("ACL_CODE")) {
                        identityService.deleteMembership(userId, group.getId());
                    }
                }
            } else {
                // 创建用户对象
                identityUser = identityService.newUser(user.getSigninid());
                identityUser.setFirstName(user.getNick());
                identityUser.setLastName("");
                identityUser.setPassword(user.getPassword());
                identityUser.setEmail(user.getEmail());
                identityService.saveUser(identityUser);
            }

            // 添加membership
            String aclCode = user.getAclCode();
            if (StringUtils.isNotBlank(aclCode)) {
                String groupId = aclCode;
                org.activiti.engine.identity.Group identityGroup = identityService.createGroupQuery().groupId(groupId)
                        .singleResult();
                if (identityGroup == null) {
                    identityGroup = identityService.newGroup(groupId);
                    identityGroup.setName("机构代码");
                    identityGroup.setType("ACL_CODE");
                    identityService.saveGroup(identityGroup);
                }
                identityService.createMembership(userId, groupId);
            }

            // 添加role关联membership
            if (roleIds != null) {
                // 先删除已有角色关联
                if (activitiGroups != null) {
                    for (org.activiti.engine.identity.Group group : activitiGroups) {
                        if (group.getType().equals(Role.class.getSimpleName())) {
                            identityService.deleteMembership(userId, group.getId());
                        }
                    }
                }
                for (String roleId : roleIds) {
                    Role role = roleDao.findOne(roleId);
                    String groupId = role.getCode();
                    org.activiti.engine.identity.Group identityGroup = identityService.createGroupQuery()
                            .groupId(groupId).singleResult();
                    if (identityGroup == null) {
                        identityGroup = identityService.newGroup(groupId);
                        identityGroup.setName(role.getTitle());
                        identityGroup.setType(Role.class.getSimpleName());
                        identityService.saveGroup(identityGroup);
                    }
                    identityService.createMembership(userId, groupId);
                }
            }
        }
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
     * 
     * @param username
     * @return
     */
    public UserDetails loadUserDetails(String username) {
        logger.debug("Loading user details for: {}", username);

        User user = null;
        // 添加邮件登录支持
        if (username.indexOf("@") > -1) {
            user = findByProperty("email", username);
        }
        if (user == null) {
            user = findByProperty("signinid", username);
        }

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

    public void requestResetPassword(User user) {
        String email = user.getEmail();
        Assert.isTrue(StringUtils.isNotBlank(email), "User email required");
        String suject = dynamicConfigService.getString("cfg.user.reset.pwd.notify.email.title", "申请重置密码邮件");
        user.setRandomCode(UUID.randomUUID().toString());
        userDao.save(user);

        HttpServletRequest request = ServletActionContext.getRequest();
        int serverPort = request.getServerPort();
        // Reconstruct original requesting URL
        StringBuffer url = new StringBuffer();
        url.append(request.getScheme()).append("://").append(request.getServerName());
        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        String contextPath = request.getContextPath();
        if (!"/".equals(contextPath)) {
            url.append(contextPath);
        }
        url.append("/pub/signin?email=" + email + "&code=" + user.getRandomCode());

        if (freemarkerService != null) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("user", user);
            params.put("resetPasswordLink", url.toString());
            String contents = freemarkerService.processTemplateByFileName("PASSWORD_RESET_NOTIFY_EMAIL", params);
            mailService.sendHtmlMail(suject, contents, true, email);
        } else {
            mailService.sendHtmlMail(suject, url.toString(), true, email);
        }
    }

    public void resetPassword(User user, String rawPassword) {
        user.setRandomCode(null);
        save(user, rawPassword);
    }

    public List<User> findByAclCode(String aclCode) {
        return userDao.findByAclCode(aclCode);
    }

    /**
     * 基于应用的用户数据重置Activiti工作流引擎的用户和组数据
     */
    public void resetActivitiIndentityData() {
        Iterable<User> users = userDao.findAll();
        for (User user : users) {
            List<UserR2Role> userR2Roles = user.getUserR2Roles();
            String[] roleIds = new String[userR2Roles.size()];
            for (int i = 0; i < roleIds.length; i++) {
                roleIds[i] = userR2Roles.get(i).getRole().getId();
            }
            cascadeActivitiIndentityData(user, roleIds);
        }
    }
}
