package lab.s2jh.auth.security;

import lab.s2jh.auth.dao.UserDao;
import lab.s2jh.auth.entity.User;
import lab.s2jh.core.context.SpringContextHolder;
import lab.s2jh.core.security.AuthContextHolder;

/**
 * 基于Spring Security获取当前登录用户对象的帮助类
 *
 */
public class AuthUserHolder {

    public static User getLogonUser() {
        UserDao userDao = SpringContextHolder.getBean(UserDao.class);
        return userDao.findByUid(AuthContextHolder.getAuthUserDetails().getUid());
    }
}
