package lab.s2jh.auth.security;

import lab.s2jh.auth.service.UserService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 扩展标准的JDBC权限访问接口，实现额外的用户信息查询以及用户关联权限代码集合查询处理
 */
public class AuthUserDetailsService implements UserDetailsService {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.loadUserDetails(username);
    }
}
