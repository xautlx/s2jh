package lab.s2jh.pub.web.action;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.context.KernelConfigParameters;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.RestActionSupport;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;

/**
 * 登录处理
 */
public class SigninController extends RestActionSupport {

	@Autowired
	private KernelConfigParameters kernelConfigParameters;

	public String execute() {
		return "/pub/signin";
	}

	public boolean isDevMode() {
		return kernelConfigParameters.isDevMode();
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
