package lab.s2jh.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.web.SimpleController;
import lab.s2jh.ctx.DynamicConfigService;
import lab.s2jh.sys.service.MenuService;
import lab.s2jh.sys.vo.NavMenuVO;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 全局布局处理
 */
public class LayoutController extends SimpleController {

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    public String getSystemTitle() {
        return dynamicConfigService.getSystemTitle();
    }

    public AuthUserDetails getAuthUserDetails() {
        return AuthContextHolder.getAuthUserDetails();
    }

    public String getBaiduMapAppid() {
        return dynamicConfigService.getString("baidu.map.appid");
    }

    public HttpHeaders index() {
        return start();
    }

    public HttpHeaders start() {
        HttpServletRequest request = ServletActionContext.getRequest();
        List<NavMenuVO> menus = menuService.authUserMenu(AuthContextHolder.getAuthUserDetails().getAuthorities(),
                request.getContextPath());
        request.setAttribute("rootMenus", menus);
        return buildDefaultHttpHeaders("start");
    }

    public HttpHeaders dashboard() {
        return buildDefaultHttpHeaders("dashboard");
    }

    public HttpHeaders exception() {
        if (true) {
            throw new IllegalArgumentException("Mock Exception");
        }
        return buildDefaultHttpHeaders("mock");
    }
}
