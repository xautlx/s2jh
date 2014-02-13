package lab.s2jh.core.web;

import java.io.Serializable;

import lab.s2jh.core.entity.BaseEntity;

import org.apache.commons.lang3.StringUtils;

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

    @Override
    public void prepareEdit() {
        String id = this.getParameter("id");
        if (StringUtils.isBlank(id)) {
            ID clone = getId("clone");
            if (clone == null) {
                newBindingEntity();
            } else {
                setupDetachedBindingEntity(clone);
                bindingEntity.setId(null);
                bindingEntity.setVersion(0);
            }
        }
    }
}
