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
import com.google.common.collect.Maps;

@MappedSuperclass
@JsonInclude(Include.NON_NULL)
public abstract class PersistableEntity<ID extends Serializable> implements Persistable<ID> {

    /** Entity本身无用，主要用于UI层辅助参数传递 */
    private Map<String, Object> extraAttributes;

    @Transient
    public String getDisplayId() {
        Serializable id = getId();
        if (id == null) {
            return "";
        }
        if (id != null && id instanceof String) {
            String idStr = (String) id;
            if (StringUtils.isNotBlank(idStr)) {
                int length = idStr.length();
                return idStr.substring(length - 10, length);
            }
        }
        return id.toString();
    }

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
    public abstract String getDisplayLabel();

    @Transient
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
}
