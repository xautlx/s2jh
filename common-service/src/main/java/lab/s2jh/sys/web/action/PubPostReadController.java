package lab.s2jh.sys.web.action;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.sys.entity.PubPostRead;
import lab.s2jh.sys.service.PubPostReadService;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData(title = "公告阅读记录")
public class PubPostReadController extends BaseController<PubPostRead,String> {

    @Autowired
    private PubPostReadService pubPostReadService;

    @Override
    protected BaseService<PubPostRead, String> getEntityService() {
        return pubPostReadService;
    }
    
    @Override
    protected void checkEntityAclPermission(PubPostRead entity) {
        // TODO Add acl check code logic
    }

    @Override
    @MetaData(title = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }
}