package lab.s2jh.pub.web.action;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.service.PropertiesConfigService;
import lab.s2jh.core.web.SimpleController;
import lab.s2jh.ctx.DynamicConfigService;
import lab.s2jh.ctx.MailService;

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
    private PropertiesConfigService propertiesConfigService;

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
        return propertiesConfigService.isDevMode();
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
}
