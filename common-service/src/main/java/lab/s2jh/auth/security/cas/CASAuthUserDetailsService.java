package lab.s2jh.auth.security.cas;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lab.s2jh.auth.service.PrivilegeService;
import lab.s2jh.auth.service.RoleService;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.security.AclService;

import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
		Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();
		String username = assertion.getPrincipal().getName();
		Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
		if (logger.isDebugEnabled()) {
			logger.debug("Principal Attributes:");
			for (Map.Entry<String, Object> me : attributes.entrySet()) {
				logger.debug(" - {}={}", me.getKey(), me.getValue());
			}
		}

		return null;
	}

}
