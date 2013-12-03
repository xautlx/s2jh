package lab.s2jh.auth.security.cas;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lab.s2jh.auth.entity.Privilege;
import lab.s2jh.auth.entity.Role;
import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.service.PrivilegeService;
import lab.s2jh.auth.service.RoleService;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.security.AuthUserDetails;

import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用于CAS场景,没有密码判断,直接获取用户信息，实现额外的用户信息查询以及用户关联权限代码集合查询处理
 */
public class CASAuthUserDetailsService extends AbstractCasAssertionUserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(CASAuthUserDetailsService.class);

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
	public UserDetails loadUserDetails(Assertion assertion) {
		String username = assertion.getPrincipal().getName();
		if (logger.isDebugEnabled()) {
			Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
			logger.debug("Principal Attributes:");
			for (Map.Entry<String, Object> me : attributes.entrySet()) {
				logger.debug(" - {}={}", me.getKey(), me.getValue());
			}
		}

		User user = userService.findByOauthUser(username);
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
		boolean superAdminUser = false;
		List<Role> roles = roleService.findR2RolesForUser(user);
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
		authUserDetails.setAclCode(user.getAclCode());
		authUserDetails.setAclType(user.getAclType());
		authUserDetails.setEmail(user.getEmail());

		if (aclService != null) {
			authUserDetails.setAclCodePrefixs(aclService.getStatAclCodePrefixs(user.getAclCode()));
		}

		// 处理用户拥有的权限代码集合
		Set<String> privilegeCodeSet = new HashSet<String>();
		if (superAdminUser) {
			List<Privilege> privileges = privilegeService.findAllCached();
			for (Privilege privilege : privileges) {
				privilegeCodeSet.add(privilege.getCode().trim());
			}
		} else {
			List<Privilege> privileges = userService.findRelatedPrivilegesForUser(user);
			for (Privilege privilege : privileges) {
				privilegeCodeSet.add(privilege.getCode().trim());
			}
		}
		authUserDetails.setPrivilegeCodes(privilegeCodeSet);

		return authUserDetails;
	}

}
