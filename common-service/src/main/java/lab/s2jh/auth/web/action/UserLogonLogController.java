package lab.s2jh.auth.web.action;

import lab.s2jh.auth.entity.UserLogonLog;
import lab.s2jh.auth.service.UserLogonLogService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData(title = "登录记录")
public class UserLogonLogController extends BaseController<UserLogonLog, String> {

    @Autowired
    private UserLogonLogService userLogonLogService;

    @Override
    protected BaseService<UserLogonLog, String> getEntityService() {
        return userLogonLogService;
    }

    @Override
    protected void checkEntityAclPermission(UserLogonLog entity) {
        // Allow all
    }

    @Override
    @MetaData(title = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }
}