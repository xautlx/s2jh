/**
 * Copyright (c) 2012
 */
package lab.s2jh.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.audit.SaveUpdateAuditListener;
import lab.s2jh.core.entity.annotation.SkipParamBind;
import lab.s2jh.core.entity.def.DefaultAuditable;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonInclude(Include.NON_EMPTY)
@EntityListeners({ SaveUpdateAuditListener.class })
@MappedSuperclass
@AuditOverrides({ @AuditOverride(forClass = BaseEntity.class) })
public abstract class BaseEntity<ID extends Serializable> extends PersistableEntity<ID> implements
        DefaultAuditable<String, ID> {

    /** 乐观锁版本,初始设置为0 */
    private int version = 0;

    @MetaData(value = "数据访问控制代码", tooltips = "用于分机构的数据访问控制代码")
    protected String aclCode;

    /** 数据访问控制类型 */
    protected String aclType;

    protected String createdBy;

    protected Date createdDate;

    protected String lastModifiedBy;

    protected Date lastModifiedDate;

    public abstract void setId(final ID id);

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Auditable#getCreatedBy()
     */
    @JsonProperty
    @Column(updatable = false, name = "created_by")
    public String getCreatedBy() {
        return createdBy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.domain.Auditable#setCreatedBy(java.lang.Object)
     */
    @SkipParamBind
    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(updatable = false, name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonProperty
    public Date getCreatedDate() {
        return createdDate;
    }

    @SkipParamBind
    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Auditable#getLastModifiedBy()
     */
    @JsonIgnore
    @Column(name = "updated_by")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    @SkipParamBind
    public void setLastModifiedBy(final String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_dt")
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(final Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Column(length = 20, nullable = true)
    public String getAclCode() {
        return aclCode;
    }

    @SkipParamBind
    public void setAclCode(String aclCode) {
        this.aclCode = aclCode;
    }

    public String getAclType() {
        return aclType;
    }

    @SkipParamBind
    public void setAclType(String aclType) {
        this.aclType = aclType;
    }

    @Version
    @Column(nullable = true)
    @JsonProperty
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void resetCommonProperties() {
        setId(null);
        version = 0;
        lastModifiedBy = null;
        lastModifiedDate = null;
        createdBy = null;
        createdDate = null;
        aclCode = null;
        aclType = null;
        addExtraAttribute(PersistableEntity.EXTRA_ATTRIBUTE_DIRTY_ROW, true);
    }

    private static final String[] PROPERTY_LIST = new String[] { "id", "version", "lastModifiedBy", "lastModifiedDate",
            "createdBy", "createdDate", "aclCode", "aclType" };

    public String[] retriveCommonProperties() {
        return PROPERTY_LIST;
    }
}
