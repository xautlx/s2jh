package lab.s2jh.common.web.action;

import lab.s2jh.core.context.KernelConfigParameters;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.rest.RestActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Namespace("/pub")
public class SigninController extends RestActionSupport {

    @Autowired
    private KernelConfigParameters kernelConfigParameters;

    public String execute() {
        return "/pub/signin";
    }

    public boolean isDevMode() {
        return kernelConfigParameters.isDevMode();
    }

}
