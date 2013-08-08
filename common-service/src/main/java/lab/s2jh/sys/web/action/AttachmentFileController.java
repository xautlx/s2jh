package lab.s2jh.sys.web.action;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.exception.WebException;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.util.ServletUtils;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.sys.entity.AttachmentFile;
import lab.s2jh.sys.service.AttachmentFileService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import com.google.common.collect.Lists;

@MetaData(title = "附件处理")
public class AttachmentFileController extends BaseController<AttachmentFile, String> {

    @Autowired
    private AttachmentFileService attachmentFileService;

    private File[] attachments;

    private String[] attachmentsFileName;

    private String[] attachmentsContentType;

    @Override
    protected BaseService<AttachmentFile, String> getEntityService() {
        return attachmentFileService;
    }

    public File[] getAttachments() {
        return attachments;
    }

    public void setAttachments(File[] attachments) {
        this.attachments = attachments;
    }

    public String[] getAttachmentsFileName() {
        return attachmentsFileName;
    }

    public void setAttachmentsFileName(String[] attachmentsFileName) {
        this.attachmentsFileName = attachmentsFileName;
    }

    public String[] getAttachmentsContentType() {
        return attachmentsContentType;
    }

    public void setAttachmentsContentType(String[] attachmentsContentType) {
        this.attachmentsContentType = attachmentsContentType;
    }

    @Override
    protected void checkEntityAclPermission(AttachmentFile entity) {
        //Do nothing check
    }

    @Override
    @MetaData(title = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @MetaData(title = "单文件上传")
    public HttpHeaders uploadSingle() {
        try {
            Assert.isTrue(attachments.length == 1, "上传文件数据不合法");
            File attachment = attachments[0];
            byte[] fileBytes = FileUtils.readFileToByteArray(attachment);
            String md5 = DigestUtils.md5DigestAsHex(fileBytes);
            AttachmentFile entity = attachmentFileService.findOne(md5);
            if (entity == null) {
                entity = new AttachmentFile();
                entity.setId(md5);
                entity.setFileContent(fileBytes);
            }
            entity.setFileRealName(attachmentsFileName[0]);
            entity.setFileLength((int) attachment.length());
            entity.setFileType(attachmentsContentType[0]);
            entity.setFileExtension(StringUtils.substringAfterLast(entity.getFileRealName(), "."));
            attachmentFileService.save(entity);
            setModel(OperationResult.buildSuccessResult("文件上传操作成功", entity));
            return buildDefaultHttpHeaders();
        } catch (Exception e) {
            throw new WebException(e.getMessage(), e);
        }
    }

    @MetaData(title = "多文件上传")
    public HttpHeaders uploadMulti() {
        try {
            if (attachments != null && attachments.length > 0) {
                List<AttachmentFile> entities = Lists.newArrayList();
                for (int i = 0; i < attachments.length; i++) {
                    File attachment = attachments[i];
                    byte[] fileBytes = FileUtils.readFileToByteArray(attachment);
                    AttachmentFile entity = new AttachmentFile();
                    entity.setId(DigestUtils.md5DigestAsHex(fileBytes));
                    entity.setFileRealName(attachmentsFileName[i]);
                    entity.setFileLength((int) attachment.length());
                    entity.setFileType(attachmentsContentType[i]);
                    entity.setFileContent(fileBytes);
                    entity.setFileExtension(StringUtils.substringAfterLast(entity.getFileRealName(), "."));
                    entities.add(entity);
                }
                attachmentFileService.save(entities);
                setModel(OperationResult.buildSuccessResult("文件上传操作成功", entities));
            } else {
                setModel(OperationResult.buildFailureResult("无上传文件"));
            }
            return buildDefaultHttpHeaders();
        } catch (Exception e) {
            throw new WebException(e.getMessage(), e);
        }
    }

    @MetaData(title = "文件下载")
    public void download() {
        AttachmentFile entity = attachmentFileService.findOne(this.getRequiredParameter("id"));
        HttpServletResponse response = ServletActionContext.getResponse();
        ServletUtils.setFileDownloadHeader(response, entity.getFileRealName());
        response.setContentType(entity.getFileType());
        ServletUtils.renderFileDownload(response, entity.getFileContent());
    }
}
