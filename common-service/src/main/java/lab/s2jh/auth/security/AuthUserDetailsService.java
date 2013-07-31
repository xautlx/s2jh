package lab.s2jh.auth.security;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lab.s2jh.auth.entity.Privilege;
import lab.s2jh.auth.entity.Role;
import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.service.PrivilegeService;
import lab.s2jh.auth.service.RoleService;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.security.AuthUserDetails;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 扩展标准的JDBC权限访问接口，实现额外的用户信息查询以及用户关联权限代码集合查询处理
 */
public class AuthUserDetailsService implements UserDetailsService {

    private UserService userService;

    private PrivilegeService privilegeService;

    private RoleService roleService;

    private AclService aclService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setPrivilegeService(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public void setAclService(AclService aclService) {
        this.aclService = aclService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] usernameSplits = username.split("#");
        User user = userService.findByAclCodeAndSigninid(usernameSplits.length == 1 ? null : usernameSplits[0],
                usernameSplits.length == 1 ? usernameSplits[0] : usernameSplits[1]);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }

        boolean enabled = user.getDisabled() == null ? true : !user.getDisabled();
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
        boolean superAdminUser = false;
        List<Role> roles = roleService.findR2RolesForUser(user.getId());
        for (Role role : roles) {
            String roleCode = role.getCode();
            dbAuthsSet.add(new SimpleGrantedAuthority(roleCode));
            if (Role.ROLE_ADMIN_CODE.equals(roleCode)) {
                superAdminUser = true;
            }
        }
        dbAuthsSet.add(new SimpleGrantedAuthority(Role.ROLE_ANONYMOUSLY_CODE));

        AuthUserDetails authUserDetails = new AuthUserDetails(username, user.getPassword(), enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, dbAuthsSet);
        authUserDetails.setUid(user.getUid());
        authUserDetails.setUsernameDisplay(user.getNick());
        authUserDetails.setAclCode(user.getAclCode());
        authUserDetails.setAclType(user.getAclType());
        authUserDetails.setEmail(user.getEmail());

        if(aclService!=null){
            authUserDetails.setAclCodePrefixs(aclService.getStatAclCodePrefixs(user.getAclCode()));
        }
        
        //处理用户拥有的权限代码集合
        Set<String> privilegeCodeSet = new HashSet<String>();
        if (superAdminUser) {
            List<Privilege> privileges = privilegeService.findAllCached();
            for (Privilege privilege : privileges) {
                privilegeCodeSet.add(privilege.getCode().trim());
            }
        } else {
            List<Privilege> privileges = userService.findRelatedPrivilegesForUser(user.getId());
            for (Privilege privilege : privileges) {
                privilegeCodeSet.add(privilege.getCode().trim());
            }
        }
        authUserDetails.setPrivilegeCodes(privilegeCodeSet);

        return authUserDetails;
    }

}
