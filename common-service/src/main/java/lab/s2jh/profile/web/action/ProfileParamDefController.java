package lab.s2jh.profile.web.action;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.profile.entity.ProfileParamDef;
import lab.s2jh.profile.service.ProfileParamDefService;
import lab.s2jh.web.action.BaseController;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData("个性化配置参数定义管理")
public class ProfileParamDefController extends BaseController<ProfileParamDef, String> {

    @Autowired
    private ProfileParamDefService profileParamDefService;

    @Override
    protected BaseService<ProfileParamDef, String> getEntityService() {
        return profileParamDefService;
    }

    @Override
    protected void checkEntityAclPermission(ProfileParamDef entity) {
        // TODO Add acl check code logic
    }

    @MetaData("[TODO方法作用]")
    public HttpHeaders todo() {
        //TODO
        setModel(OperationResult.buildSuccessResult("TODO操作完成"));
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData("保存")
    public HttpHeaders doSave() {
        return super.doSave();
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