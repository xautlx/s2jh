package lab.s2jh.pub.web.action;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.context.KernelConfigParameters;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;

/**
 * 登录处理
 */
public class SigninController extends BaseController<User, Long> {

	@Autowired
	private KernelConfigParameters kernelConfigParameters;

	@Autowired
	private UserService userService;

    public HttpHeaders index() {
        return new DefaultHttpHeaders("/pub/signin").disableCaching();
    }

	public boolean isDevMode() {
		return kernelConfigParameters.isDevMode();
	}

	public String getSystemTitle() {
		return kernelConfigParameters.getSystemTitle();
	}

	public boolean isSignupDisabled() {
		return kernelConfigParameters.isSignupDisabled();
	}

	public boolean isCasSupport() {
		return casAuthenticationEntryPoint != null;
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
	

	@Override
	protected BaseService<User, Long> getEntityService() {
		return userService;
	}

	@Override
	protected void checkEntityAclPermission(User entity) {
		//Do nothing
	}
}
