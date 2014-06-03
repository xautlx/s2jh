package lab.s2jh.auth.web.action;

import java.util.List;

import lab.s2jh.auth.entity.Role;
import lab.s2jh.auth.entity.SignupUser;
import lab.s2jh.auth.service.RoleService;
import lab.s2jh.auth.service.SignupUserService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.web.action.BaseController;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData("注册账号管理")
public class SignupUserController extends BaseController<SignupUser, String> {

    @Autowired
    private SignupUserService signupUserService;

    @Autowired
    private RoleService roleService;

    @Override
    protected BaseService<SignupUser, String> getEntityService() {
        return signupUserService;
    }

    @Override
    protected void checkEntityAclPermission(SignupUser entity) {
        // Nothing to do
    }

    public List<Role> getRoles() {
        return roleService.findAllCached();
    }

    @MetaData("审核")
    public HttpHeaders doAudit() {
        String aclCode = this.getParameter("aclCode");
        bindingEntity.setAclCode(aclCode);
        signupUserService.audit(bindingEntity, roleService.findAll(getParameterIds("r2ids")));
        setModel(OperationResult.buildSuccessResult("已审核处理并创建对应用户记录", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData("删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData("查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }
}