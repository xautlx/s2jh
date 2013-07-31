/**
 * Copyright (c) 2012
 */
package lab.s2jh.core.security;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 扩展Spring Security的UserDetails对象，追加email，权限代码集合等信息
 */
public class AuthUserDetails implements UserDetails {
    
    /** 全局唯一的用户标识 */
    private String uid;

	/** The password. */
	private String password;

	/** The username. */
	private String username;
	
	private String email;

	/** The authorities. */
	private Set<GrantedAuthority> authorities;

	/** The account non expired. */
	private boolean accountNonExpired=true;

	/** The account non locked. */
	private boolean accountNonLocked=true;

	/** The credentials non expired. */
	private boolean credentialsNonExpired=true;

	/** The enabled. */
	private boolean enabled;

	/** The privilege codes. */
	private Collection<String> privilegeCodes;
	
	/** (中文)显示名称 */
	private String usernameDisplay;
	
    /** 数据访问控制代码 */
    private String aclCode;
    
    /** 数据访问控制类型(数字大权限包含数字小) */
    private Integer aclType;
    
    /** 扩展属性容器，如CAS Oauth认证返回用户信息 */
    private Map<String, Object> attributes;
    
    /**
     * 基于一个单一ACL Code返回其可以访问的ACL Code前缀集合
     * 如用户ACL Code为120000，根据业务规则其访问前缀集合可转化12, AA12,BB12等
     * @see AclService#getStatAclCodePrefixs(String)
     */
    private Collection<String> aclCodePrefixs;

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the authorities.
	 * 
	 * @return the authorities
	 */
	public Set<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * Sets the authorities.
	 * 
	 * @param authorities
	 *            the authorities to set
	 */
	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities =authorities;
	}

	/**
	 * Checks if is account non expired.
	 * 
	 * @return the accountNonExpired
	 */
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	/**
	 * Sets the account non expired.
	 * 
	 * @param accountNonExpired
	 *            the accountNonExpired to set
	 */
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	/**
	 * Checks if is account non locked.
	 * 
	 * @return the accountNonLocked
	 */
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	/**
	 * Sets the account non locked.
	 * 
	 * @param accountNonLocked
	 *            the accountNonLocked to set
	 */
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * Checks if is credentials non expired.
	 * 
	 * @return the credentialsNonExpired
	 */
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	/**
	 * Sets the credentials non expired.
	 * 
	 * @param credentialsNonExpired
	 *            the credentialsNonExpired to set
	 */
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	/**
	 * Checks if is enabled.
	 * 
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled.
	 * 
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Gets the privilege codes.
	 * 
	 * @return the privilegeCodes
	 */
	public Collection<String> getPrivilegeCodes() {
		return privilegeCodes;
	}

	/**
	 * Sets the privilege codes.
	 * 
	 * @param privilegeCodes
	 *            the privilegeCodes to set
	 */
	public void setPrivilegeCodes(Collection<String> privilegeCodes) {
		this.privilegeCodes = privilegeCodes;
	}

	/**
	 * Construct the <code>User</code> with the details required by.
	 * 
	 * @param username
	 *            the username presented to the
	 *            <code>DaoAuthenticationProvider</code>
	 * @param password
	 *            the password that should be presented to the
	 *            <code>DaoAuthenticationProvider</code>
	 * @param enabled
	 *            set to <code>true</code> if the user is enabled
	 * @param accountNonExpired
	 *            set to <code>true</code> if the account has not expired
	 * @param credentialsNonExpired
	 *            set to <code>true</code> if the credentials have not expired
	 * @param accountNonLocked
	 *            set to <code>true</code> if the account is not locked
	 * @param authorities
	 *            the authorities that should be granted to the caller if they
	 *            presented the correct username and password and the user is
	 *            enabled
	 * @throws IllegalArgumentException
	 *             if a <code>null</code> value was passed either as a parameter
	 *             or as an element in the <code>GrantedAuthority[]</code> array
	 *             {@link org.springframework.security.providers.dao.DaoAuthenticationProvider}
	 *             .
	 */
	public AuthUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Set<GrantedAuthority> authorities)
			throws IllegalArgumentException {
		if (((username == null) || "".equals(username)) || (password == null)) {
			throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
		}

		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities=authorities;
	}
	
	   public AuthUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
	            boolean credentialsNonExpired, boolean accountNonLocked, Set<GrantedAuthority> authorities,Map<String, Object> attributes)
	            throws IllegalArgumentException {
	        this(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	        this.attributes=attributes;
	    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("username:" + username);
		sb.append("; ");
		boolean first = true;
		if (authorities != null && authorities.size() > 0) {
			sb.append("GrantedAuthority: ");
			for (GrantedAuthority grantedAuthority : authorities) {
				if (!first) {
					sb.append(",");
				}
				first = false;
				sb.append(grantedAuthority.toString());
			}
		} else {
			sb.append("Not GrantedAuthority");
		}
		sb.append("; ");
		if (privilegeCodes != null && !privilegeCodes.isEmpty()) {
			sb.append("Granted Privilege Codes: ");

			first = true;
			for (String code : privilegeCodes) {
				if (!first) {
					sb.append(",");
				}
				first = false;

				sb.append(code);
			}
		} else {
			sb.append("Not granted any privilege codes");
		}
		return sb.toString();
	}

    public String getUsernameDisplay() {
        return usernameDisplay;
    }

    public void setUsernameDisplay(String usernameDisplay) {
        this.usernameDisplay = usernameDisplay;
    }
    public String getAclCode() {
        return aclCode;
    }

    public void setAclCode(String aclCode) {
        this.aclCode = aclCode;
    }
    

    public Integer getAclType() {
        return aclType;
    }

    public void setAclType(Integer aclType) {
        this.aclType = aclType;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Collection<String> getAclCodePrefixs() {
        return aclCodePrefixs;
    }

    public void setAclCodePrefixs(Collection<String> aclCodePrefixs) {
        this.aclCodePrefixs = aclCodePrefixs;
    }
}
