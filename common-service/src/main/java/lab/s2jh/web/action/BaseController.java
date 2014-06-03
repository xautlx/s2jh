package lab.s2jh.web.action;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.context.SpringContextHolder;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.web.PersistableController;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.sys.entity.AttachmentFile;
import lab.s2jh.sys.service.AttachmentFileService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.struts2.rest.HttpHeaders;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.opensymphony.xwork2.inject.Inject;

public abstract class BaseController<T extends BaseEntity<ID>, ID extends Serializable> extends
        PersistableController<T, ID> {

    protected long imageUploadMaxSize;

    @Inject(value = "struts.image.upload.maxSize", required = false)
    public void setImageUploadMaxSize(String imageUploadMaxSize) {
        this.imageUploadMaxSize = Long.parseLong(imageUploadMaxSize);
    }

    public Long getImageUploadMaxSize() {
        return imageUploadMaxSize;
    }

    protected void prepareClone() {
        //子类根据需要添加克隆对象初始化代码
    }

    @Override
    public void prepareEdit() {
        super.prepareEdit();
        String clone = this.getParameter("clone");
        if (BooleanUtils.toBoolean(clone)) {
            bindingEntity.setId(null);
            bindingEntity.setVersion(0);
            prepareClone();
        }
    }

    @MetaData(value = "保存")
    protected HttpHeaders doSave() {
        getEntityService().save(bindingEntity);

        //附件关联处理，自动处理以attachment为前缀的参数
        Enumeration<?> names = getRequest().getParameterNames();
        Set<String> attachmentNames = Sets.newHashSet();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            if (name.startsWith("attachment")) {
                attachmentNames.add(name);
            }
        }
        if (attachmentNames.size() > 0) {
            AttachmentFileService attachmentFileService = SpringContextHolder.getBean(AttachmentFileService.class);
            for (String attachmentName : attachmentNames) {
                String[] attachments = getParameterIds(attachmentName);
                attachmentFileService.attachmentBind(attachments, bindingEntity, attachmentName);
            }
        }
        setModel(OperationResult.buildSuccessResult("数据保存成功", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    protected HttpHeaders attachmentList(T entity, String category) {
        AttachmentFileService attachmentFileService = SpringContextHolder.getBean(AttachmentFileService.class);
        List<AttachmentFile> attachmentFiles = attachmentFileService.findBy(entity.getClass().getName(),
                String.valueOf(entity.getId()), category);

        List<Map<String, Object>> filesResponse = Lists.newArrayList();
        for (AttachmentFile attachmentFile : attachmentFiles) {
            Map<String, Object> dataMap = Maps.newHashMap();
            dataMap.put("id", attachmentFile.getId());
            dataMap.put("attachmentName", attachmentFile.getEntityFileCategory());
            dataMap.put("name", attachmentFile.getFileRealName());
            dataMap.put("size", FileUtils.byteCountToDisplaySize(attachmentFile.getFileLength()));
            dataMap.put("url",
                    getRequest().getContextPath() + "/sys/attachment-file!download?id=" + attachmentFile.getId());
            filesResponse.add(dataMap);
        }
        Map<String, List<Map<String, Object>>> response = Maps.newHashMap();
        response.put("files", filesResponse);
        setModel(response);
        return buildDefaultHttpHeaders();
    }
}
