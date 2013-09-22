/**
 * Copyright (c) 2012
 */
package lab.s2jh.core.security;

import java.util.Collection;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * 以ThreadLocal方式实现Web端登录信息传递到业务层的存取
 */
public class AuthContextHolder {

    public static final String SPRING_SECURITY_LAST_USERNAME_KEY = "SPRING_SECURITY_LAST_USERNAME";

    private static final ThreadLocal<String> userPinContainer = new ThreadLocal<String>();

    private static final ThreadLocal<Locale> localeContainer = new ThreadLocal<Locale>();

    public static final String DEFAULT_UNKNOWN_PIN = "N/A";
    
    public static final Locale DEFAULT_LOCALE = new Locale("zh", "cn");

    public static void setAuthUserPin(String userPin) {
        userPinContainer.set(userPin);
    }

    /**
     * 获取用户唯一登录标识
     */
    public static String getAuthUserPin() {
        String pin = userPinContainer.get();
        if (StringUtils.isBlank(pin)) {
            AuthUserDetails authUserDetails = getAuthUserDetails();
            if (authUserDetails != null && authUserDetails.getUsername() != null) {
                pin = authUserDetails.getUsername();
            } else {
                pin = DEFAULT_UNKNOWN_PIN;
            }
        }
        return pin;
    }

    /**
     * 基于Spring Security获取用户认证信息
     */
    public static AuthUserDetails getAuthUserDetails() {
        AuthUserDetails userDetails = null;
        if (SecurityContextHolder.getContext() == null
                || SecurityContextHolder.getContext().getAuthentication() == null
                || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthUserDetails) {
            userDetails = (AuthUserDetails) principal;
        }
        Assert.notNull(userDetails);
        return userDetails;
    }

    /**
     * 获取用户ACL CODE
     */
    public static String getAclCode() {
        return getAuthUserDetails().getAclCode();
    }

    /**
     * 获取用户ACL CODE
     */
    public static String getAclCodePrefix() {
        String aclCode = getAclCode();
        if (StringUtils.isBlank(aclCode)) {
            return "";
        }
        Collection<String> aclCodePrefixs = getAclCodePrefixs();
        if (CollectionUtils.isEmpty(aclCodePrefixs)) {
            return "";
        }
        for (String aclCodePrefix : aclCodePrefixs) {
            if (aclCode.startsWith(aclCodePrefix)) {
                return aclCodePrefix;
            }
        }
        throw new IllegalStateException("ACL前缀计算异常");
    }

    public static Collection<String> getAclCodePrefixs() {
        return getAuthUserDetails().getAclCodePrefixs();
    }

    /**
     * 获取Locale
     */
    public static Locale getLocale() {
        Locale locale = localeContainer.get();
        if (locale == null) {
            return DEFAULT_LOCALE;
        }
        return locale;
    }
}
