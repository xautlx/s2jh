package lab.s2jh.auth.security;

import lab.s2jh.auth.dao.UserDao;
import lab.s2jh.auth.entity.Department;
import lab.s2jh.auth.entity.User;
import lab.s2jh.core.context.SpringContextHolder;
import lab.s2jh.core.security.AuthContextHolder;

/**
 * 基于Spring Security获取当前登录用户对象的帮助类
 *
 */
public class AuthUserHolder {

    /**
     * 获取当前登录用户对象
     */
    public static User getLogonUser() {
        UserDao userDao = SpringContextHolder.getBean(UserDao.class);
        return userDao.findByUid(AuthContextHolder.getAuthUserDetails().getUid());
    }

    /**
     * 获取当前登录用户所属部门对象
     */
    public static Department getLogonUserDepartment() {
        User user = getLogonUser();
        if (user == null) {
            return null;
        }
        return user.getDepartment();
    }
}
