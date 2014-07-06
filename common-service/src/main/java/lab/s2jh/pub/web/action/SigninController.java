package lab.s2jh.pub.web.action;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.PropertiesConfigService;
import lab.s2jh.core.web.SimpleController;
import lab.s2jh.core.web.captcha.ImageCaptchaServlet;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.ctx.DynamicConfigService;
import lab.s2jh.ctx.MailService;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;

/**
 * 登录处理
 */
public class SigninController extends SimpleController {
    
    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    public HttpHeaders index() {
        return new DefaultHttpHeaders("/pub/signin").disableCaching();
    }

    public boolean isDevMode() {
        return PropertiesConfigService.isDevMode();
    }

    public String getSystemTitle() {
        return dynamicConfigService.getSystemTitle();
    }

    public boolean isSignupEnabled() {
        return !dynamicConfigService.isSignupDisabled();
    }

    public boolean isCasSupport() {
        return casAuthenticationEntryPoint != null;
    }

    public boolean isMailServiceEnabled() {
        return mailService.isEnabled();
    }

    @Autowired(required = false)
    private CasAuthenticationEntryPoint casAuthenticationEntryPoint;

    public String getCasRedirectUrl() {
        HttpServletRequest request = ServletActionContext.getRequest();
        final StringBuilder buffer = new StringBuilder();
        buffer.append(request.isSecure() ? "https://" : "http://");
        buffer.append(request.getServerName());
        buffer.append(request.getServerPort() == 80 ? "" : ":" + request.getServerPort());
        buffer.append(request.getContextPath());
        buffer.append("/j_spring_cas_security_check");

        final String urlEncodedService = ServletActionContext.getResponse().encodeURL(buffer.toString());
        final String redirectUrl = CommonUtils.constructRedirectUrl(casAuthenticationEntryPoint.getLoginUrl(),
                casAuthenticationEntryPoint.getServiceProperties().getServiceParameter(), urlEncodedService,
                casAuthenticationEntryPoint.getServiceProperties().isSendRenew(), false);
        return redirectUrl;
    }

    @MetaData("请求找回密码")
    public HttpHeaders forget() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String uid = getRequiredParameter("uid");
        String jCaptcha = getRequiredParameter("j_captcha");
        if (!ImageCaptchaServlet.validateResponse(request, jCaptcha)) {
            setModel(OperationResult.buildFailureResult("验证码不正确，请重新输入"));
        } else {
            User user = null;
            if (uid.indexOf("@") > -1) {
                user = userService.findByProperty("email", uid);
            }
            if (user == null) {
                user = userService.findBySigninid(uid);
            }
            if (user == null) {
                setModel(OperationResult.buildFailureResult("未找到匹配账号信息，请联系管理员处理"));
            } else {
                String email = user.getEmail();
                if (StringUtils.isBlank(email)) {
                    setModel(OperationResult.buildFailureResult("当前账号未设定注册邮箱，请联系管理员先设置邮箱后再进行此操作"));
                } else {
                    userService.requestResetPassword(user);
                    setModel(OperationResult.buildSuccessResult("找回密码请求处理成功", user.getEmail()));
                }
            }
        }
        return buildDefaultHttpHeaders();
    }

    @MetaData("重置密码")
    public HttpHeaders resetpwd() {
        String email = getRequiredParameter("email");
        String code = getRequiredParameter("code");
        String password = getRequiredParameter("password");
        User user = userService.findByProperty("email", email);
        if (user == null) {
            setModel(OperationResult.buildFailureResult("未找到匹配账号信息，请联系管理员处理"));
            return buildDefaultHttpHeaders();
        }
        if (!code.equals(user.getRandomCode())) {
            setModel(OperationResult.buildFailureResult("校验码已过期，请重新找回密码操作"));
            return buildDefaultHttpHeaders();
        }

        userService.resetPassword(user, password);
        setModel(OperationResult.buildSuccessResult("密码重置成功"));

        return buildDefaultHttpHeaders();
    }

    @MetaData("会话过期")
    public HttpHeaders expired() {
        return buildDefaultHttpHeaders("expired");
    }
}
