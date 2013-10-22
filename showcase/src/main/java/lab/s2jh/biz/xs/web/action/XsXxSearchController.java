package lab.s2jh.biz.xs.web.action;

import lab.s2jh.biz.core.web.BaseBizController;
import lab.s2jh.biz.xs.entity.XsJbxx;
import lab.s2jh.biz.xs.service.XsJbxxService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.service.BaseService;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;


@MetaData(value = "学籍查询")
public class XsXxSearchController extends BaseBizController<XsJbxx, String> {

    @Autowired
    private XsJbxxService xsJbxxService;
    
    @Autowired
    private AclService aclService;
    
    @Override
    protected BaseService<XsJbxx, String> getEntityService() {
        return xsJbxxService;
    }

    @Override
    protected void checkEntityAclPermission(XsJbxx entity) {
        aclService.validateAuthUserAclCodePermission(entity.getXxdm());
    }
    
    public AuthUserDetails getAuthUserDetails(){
        return AuthContextHolder.getAuthUserDetails();
    }
    
    @Override
    @MetaData(value = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }
}