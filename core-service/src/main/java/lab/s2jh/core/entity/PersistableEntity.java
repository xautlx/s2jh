package lab.s2jh.core.entity;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

@MappedSuperclass
@JsonInclude(Include.NON_NULL)
public abstract class PersistableEntity<ID extends Serializable> implements Persistable<ID> {

    public static final String EXTRA_ATTRIBUTE_GRID_TREE_LEVEL = "level";

    /**
     * 在批量提交处理数据时，标识对象操作类型。@see RevisionType
     */
    public static final String EXTRA_ATTRIBUTE_OPERATION = "operation";

    /** Entity本身无用，主要用于UI层辅助参数传递 */
    private Map<String, Object> extraAttributes;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Persistable#isNew()
     */
    @Transient
    @JsonIgnore
    public boolean isNew() {
        Serializable id = getId();
        return id == null || StringUtils.isBlank(String.valueOf(id));
    }

    /*
     * 用于快速判断对象是否编辑状态
     */
    @Transient
    @JsonIgnore
    public boolean isNotNew() {
        return !isNew();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        Persistable that = (Persistable) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }

    @Transient
    public abstract String getDisplay();

    @Transient
    @JsonProperty
    public Map<String, Object> getExtraAttributes() {
        return extraAttributes;
    }

    @Transient
    public void setExtraAttributes(Map<String, Object> extraAttributes) {
        this.extraAttributes = extraAttributes;
    }

    @Transient
    public void addExtraAttribute(String key, Object value) {
        if (extraAttributes == null) {
            extraAttributes = Maps.newHashMap();
        }
        extraAttributes.put(key, value);
    }

    /**
     * 从扩展属性中取值判断当前对象是否标记需要删除
     * 一般用于前端UI对关联集合对象元素移除操作
     * @return
     */
    @Transient
    @JsonIgnore
    public boolean isMarkedRemove() {
        if (extraAttributes == null) {
            return false;
        }
        Object opParams = extraAttributes.get(EXTRA_ATTRIBUTE_OPERATION);
        if (opParams == null) {
            return false;
        }
        String op = null;
        if (opParams instanceof String[]) {
            op = ((String[]) opParams)[0];
        } else if (opParams instanceof String) {
            op = (String) opParams;
        }
        if ("remove".equalsIgnoreCase(op)) {
            return true;
        }
        return false;
    }
}
