package lab.s2jh.pub.web.action;

import lab.s2jh.auth.entity.SignupUser;
import lab.s2jh.auth.service.SignupUserService;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.ctx.DynamicConfigService;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账号注册
 */
public class SignupController extends BaseController<SignupUser, String> {

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired
    private UserService userService;

    @Autowired
    private SignupUserService signupUserService;

    public HttpHeaders submit() {
        if (dynamicConfigService.isSignupDisabled()) {
            setModel(OperationResult.buildFailureResult("系统暂未开发账号注册功能，如有疑问请联系管理员"));
            return buildDefaultHttpHeaders();
        }

        String signinid = bindingEntity.getSigninid();

        if (signupUserService.findByProperty("signinid", signinid) != null) {
            setModel(OperationResult.buildFailureResult("注册账号:" + signinid + " 已被注册使用，请修改使用其他账号"));
            return buildDefaultHttpHeaders();
        }

        if (userService.findByProperty("signinid", signinid) != null) {
            setModel(OperationResult.buildFailureResult("注册账号:" + signinid + " 已被注册使用，请修改使用其他账号"));
            return buildDefaultHttpHeaders();
        }

        String email = bindingEntity.getEmail();
        if (StringUtils.isNotBlank(email)) {
            if (signupUserService.findByProperty("email", email) != null) {
                setModel(OperationResult.buildFailureResult("注册邮件:" + email + " 已被注册使用，请修改使用其他电子邮件"));
                return buildDefaultHttpHeaders();
            }

            if (userService.findByProperty("email", email) != null) {
                setModel(OperationResult.buildFailureResult("注册邮件:" + email + " 已被注册使用，请修改使用其他电子邮件"));
                return buildDefaultHttpHeaders();
            }
        }

        signupUserService.save(bindingEntity);

        setModel(OperationResult.buildSuccessResult("账号注册成功"));
        return buildDefaultHttpHeaders();
    }

    @Override
    protected BaseService<SignupUser, String> getEntityService() {
        return signupUserService;
    }

    @Override
    protected void checkEntityAclPermission(SignupUser entity) {
        //Do nothing
    }
}
