package lab.s2jh.pub.web.action;

import java.io.IOException;

import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.context.KernelConfigParameters;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户注册
 */
public class SignupController extends BaseController<User, Long> {
	
	@Autowired
	private KernelConfigParameters kernelConfigParameters;

    @Autowired
    private UserService userService;

	public String getSystemTitle() {
		return kernelConfigParameters.getSystemTitle();
	}
    
    public HttpHeaders index() {
    	if(isUserInitSignup()){
    		bindingEntity.setSigninid("admin");
    		bindingEntity.setNick("Administrator");
    	}
    	bindingEntity.setPassword("");
        return new DefaultHttpHeaders("/pub/signup").disableCaching();
    }
    
	public boolean isUserInitSignup() {
		Long userCount = userService.findUserCount();
		if (userCount == null || userCount.longValue() == 0) {
			return true;
		}
		return false;
	}

    public void submit() throws IOException {
    	if(kernelConfigParameters.isSignupDisabled()){
    		return;
    	}
    	
    	if(isUserInitSignup()){
    		bindingEntity.setEnabled(true);
    		bindingEntity.setInitSetupUser(true);
    		userService.initSetupUser(bindingEntity, bindingEntity.getPassword());
    	}else{
    		bindingEntity.setEnabled(false);
    		userService.save(bindingEntity, bindingEntity.getPassword());
    	}
        
        ServletActionContext.getResponse().sendRedirect("/pub/signin");
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
