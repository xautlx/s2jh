package lab.s2jh.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 标识对象支持关联附件处理，由框架自动处理附件相关绑定和清理工作
 * 需要支持附件关联的实体对象实现该接口，定义一个属性存放关联附件个数
 */
public interface AttachmentableEntity {

    /**
     * 关联附件个数
     * @return
     */
    @JsonProperty
    public Integer getAttachmentSize();
}
