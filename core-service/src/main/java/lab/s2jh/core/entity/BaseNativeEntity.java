/**
 * Copyright (c) 2012
 */
package lab.s2jh.core.entity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lab.s2jh.core.audit.SaveUpdateAuditListener;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 框架提供一个基础的Native方式的实体对象定义参考
 * 具体可根据项目考虑选用其他主键如自增、序列等方式，只需修改相关泛型参数类型和主键定义注解即可
 * 各属性定义可先简单定义MetaData注解即可，具体细节的控制属性含义可查看具体代码注释说明
 */
@JsonInclude(Include.NON_EMPTY)
@EntityListeners({ SaveUpdateAuditListener.class })
@MappedSuperclass
@AuditOverrides({ @AuditOverride(forClass = BaseNativeEntity.class) })
public abstract class BaseNativeEntity extends BaseEntity<Long> {

    private Long id;

    @Id
    @Column(name = "event_id")
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "native")
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
