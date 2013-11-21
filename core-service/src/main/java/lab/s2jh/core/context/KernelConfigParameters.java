package lab.s2jh.core.context;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KernelConfigParameters {

    @Value("${dev.mode:\"false\"}")
    private String devMode;
    
	@Value("${cfg.signup.disabled:\"false\"}")
	private String signupDisabled;

	@Value("${cfg.system.title:\"S2JH\"}")
	private String systemTitle;

    public boolean isDevMode() {
        return BooleanUtils.toBoolean(devMode);
    }

	public String getSystemTitle() {
		return systemTitle;
	}

	public boolean isSignupDisabled() {
		return BooleanUtils.toBoolean(signupDisabled);
	}
}
