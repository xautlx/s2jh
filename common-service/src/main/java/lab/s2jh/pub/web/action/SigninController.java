package lab.s2jh.pub.web.action;

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
		final String urlEncodedService = CommonUtils.constructServiceUrl(null, ServletActionContext.getResponse(),
				casAuthenticationEntryPoint.getServiceProperties().getService(), null, casAuthenticationEntryPoint
						.getServiceProperties().getArtifactParameter(), true);
		final String redirectUrl = CommonUtils.constructRedirectUrl(casAuthenticationEntryPoint.getLoginUrl(),
				casAuthenticationEntryPoint.getServiceProperties().getServiceParameter(), urlEncodedService,
				casAuthenticationEntryPoint.getServiceProperties().isSendRenew(), false);
		return redirectUrl;
	}
}
