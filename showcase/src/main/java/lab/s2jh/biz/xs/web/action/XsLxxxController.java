package lab.s2jh.biz.xs.web.action;

import lab.s2jh.biz.core.web.BaseBizController;
import lab.s2jh.biz.xs.entity.XsJbxx;
import lab.s2jh.biz.xs.entity.XsLxxx;
import lab.s2jh.biz.xs.service.XsJbxxService;
import lab.s2jh.biz.xs.service.XsLxxxService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.service.BaseService;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;


@MetaData(title = "学生联系信息")
public class XsLxxxController extends BaseBizController<XsLxxx,String> {

    @Autowired
    private XsLxxxService xsLxxxService;
    
    @Autowired
    private XsJbxxService xsJbxxService;
    
    @Autowired
    private AclService aclService;

    @Override
    protected BaseService<XsLxxx, String> getEntityService() {
        return xsLxxxService;
    }
    
    @Override
    protected void checkEntityAclPermission(XsLxxx entity) {
        aclService.validateAuthUserAclCodePermission(entity.getXsJbxx().getXxdm());
    }
    
    /**
     * 如果查询数据库对象不存在，则new一个对象
     */
    public void prepareUpdate() {
        if (bindingEntity == null) {
            newBindingEntity();
        }
    }
    
    /**
     * 如果查询数据库对象不存在，则new一个对象
     */
    public void prepareDoUpdate() {
        if (bindingEntity == null) {
            newBindingEntity();
        }
    }

    @Override
    @MetaData(title = "更新")
    public HttpHeaders doUpdate() {
        XsJbxx xsJbxx = xsJbxxService.findOne(getId());
        bindingEntity.setXh(xsJbxx.getXh());
        return super.doUpdate();
    }
}