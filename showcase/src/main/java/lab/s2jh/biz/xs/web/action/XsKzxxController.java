package lab.s2jh.biz.xs.web.action;

import lab.s2jh.biz.core.web.BaseBizController;
import lab.s2jh.biz.xs.entity.XsJbxx;
import lab.s2jh.biz.xs.entity.XsKzxx;
import lab.s2jh.biz.xs.service.XsJbxxService;
import lab.s2jh.biz.xs.service.XsKzxxService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.service.BaseService;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;


@MetaData(value = "学生扩展信息")
public class XsKzxxController extends BaseBizController<XsKzxx, String> {

    @Autowired
    private XsKzxxService xsKzxxService;

    @Autowired
    private XsJbxxService xsJbxxService;
    
    @Autowired
    private AclService aclService;

    @Override
    protected BaseService<XsKzxx, String> getEntityService() {
        return xsKzxxService;
    }
    
    @Override
    protected void checkEntityAclPermission(XsKzxx entity) {
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
    @MetaData(value = "更新")
    public HttpHeaders doUpdate() {
        XsJbxx xsJbxx = xsJbxxService.findOne(getId());
        bindingEntity.setXh(xsJbxx.getXh());
        return super.doUpdate();
    }
}