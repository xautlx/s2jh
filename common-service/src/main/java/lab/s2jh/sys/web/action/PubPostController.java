package lab.s2jh.sys.web.action;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.sys.entity.PubPost;
import lab.s2jh.sys.service.AttachmentFileService;
import lab.s2jh.sys.service.PubPostService;
import lab.s2jh.web.action.BaseController;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData(value = "公告管理")
public class PubPostController extends BaseController<PubPost, String> {

    @Autowired
    private PubPostService pubPostService;

    @Autowired
    private AttachmentFileService attachmentFileService;

    @Override
    protected BaseService<PubPost, String> getEntityService() {
        return pubPostService;
    }

    @Override
    protected void checkEntityAclPermission(PubPost entity) {
        // Nothing
    }

    @Override
    @MetaData(value = "保存")
    public HttpHeaders doSave() {
        return super.doSave();
    }

    @Override
    @MetaData(value = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData(value = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    @MetaData(value = "关联附件列表")
    public HttpHeaders attachmentList() {
        return attachmentList(bindingEntity, "attachments");
    }

}
