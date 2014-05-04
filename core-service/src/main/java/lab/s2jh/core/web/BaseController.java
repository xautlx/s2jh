package lab.s2jh.core.web;

import java.io.Serializable;

import lab.s2jh.core.entity.BaseEntity;

import org.apache.commons.lang3.BooleanUtils;

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
}
