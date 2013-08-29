package lab.s2jh.pub.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.service.UserService;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.RestActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * 初始化处理
 */
public class SetupController extends RestActionSupport {

    @Autowired
    private UserService userService;

    public HttpHeaders index() {
        long count = userService.findCount();
        if (count > 0) {
            this.addActionError("用户表已存在数据，不允许进行初始化设置");
        }
        return new DefaultHttpHeaders("/pub/setup").disableCaching();
    }

    public void init() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        User user = new User();
        String aclCode = request.getParameter("aclCode");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Assert.hasText(aclCode, "机构代码参数不能为空");
        Assert.hasText(username, "账号参数不能为空");
        Assert.hasText(password, "密码参数不能为空");
        user.setAclCode(aclCode);
        user.setSigninid(username);
        userService.initSetupUser(user, password);
        ServletActionContext.getResponse().sendRedirect("/pub/signin");
    }
    
    
}
