package lab.s2jh.profile.web.action;

import java.util.List;
import java.util.Map;

import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.security.AuthUserHolder;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.annotation.SecurityControlIgnore;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.profile.entity.SimpleParamVal;
import lab.s2jh.profile.service.SimpleParamValService;
import lab.s2jh.web.action.BaseController;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

@MetaData("个性化配置参数数据管理")
public class SimpleParamValController extends BaseController<SimpleParamVal, String> {

    @Autowired
    private SimpleParamValService simpleParamValService;

    @Override
    protected BaseService<SimpleParamVal, String> getEntityService() {
        return simpleParamValService;
    }

    @Override
    protected void checkEntityAclPermission(SimpleParamVal entity) {
        // TODO Add acl check code logic
    }

    @Override
    @MetaData("保存")
    public HttpHeaders doSave() {
        String[] codes = getRequiredParameter("codes").split(",");
        User user = AuthUserHolder.getLogonUser();
        for (String code : codes) {
            SimpleParamVal entity = simpleParamValService.findByUserAndCode(user, code);
            if (entity == null) {
                entity = new SimpleParamVal();
                entity.setUser(user);
            }
            entity.setCode(code);
            entity.setValue(getRequiredParameter(code));
            getEntityService().save(entity);
        }
        setModel(OperationResult.buildSuccessResult("参数默认值设定成功"));
        return buildDefaultHttpHeaders();
    }

    @MetaData("参数列表")
    @SecurityControlIgnore
    public HttpHeaders params() {
        User user = AuthUserHolder.getLogonUser();
        Map<String, String> datas = Maps.newHashMap();
        List<SimpleParamVal> simpleParams = simpleParamValService.findByUser(user);
        for (SimpleParamVal simpleParamVal : simpleParams) {
            datas.put(simpleParamVal.getCode(), simpleParamVal.getValue());
        }
        setModel(datas);
        return buildDefaultHttpHeaders();
    }
}