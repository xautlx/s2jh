package lab.s2jh.core.audit.envers;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.RevisionType;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class EntityRevision {

    /**
     * The first element will be the changed entity instance.
     */
    private Object entity;

    /**
     * The second will be an entity containing revision data 
     * (if no custom entity is used, this will be an instance of DefaultRevisionEntity)
     */
    private ExtDefaultRevisionEntity revisionEntity;

    /**
     * The third will be the type of the revision 
     * (one of the values of the RevisionType enumeration: ADD, MOD, DEL). 
     */
    private RevisionType revisionType;

    @JsonIgnore
    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public ExtDefaultRevisionEntity getRevisionEntity() {
        return revisionEntity;
    }

    public void setRevisionEntity(ExtDefaultRevisionEntity revisionEntity) {
        this.revisionEntity = revisionEntity;
    }

    public RevisionType getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(RevisionType revisionType) {
        this.revisionType = revisionType;
    }

    @JsonIgnore
    public List<RevEntityProperty> getRevEntityProperties() {
        List<RevEntityProperty> revEntityProperties = new ArrayList<RevEntityProperty>();
        Method[] methods = entity.getClass().getMethods();
        Set<String> excludeBaseProperties = Sets.newHashSet();
        for (Method method : methods) {
            if (!method.getName().startsWith("get") || method.getName().equals("getClass")
                    || method.getName().equals("getVersion")) {
                continue;
            }

            //排除非持久属性方法
            if (method.getAnnotation(Transient.class) != null) {
                continue;
            }

            //排除基类方法
            boolean skipMethod = false;
            for (String excludeProperty : excludeBaseProperties) {
                if (method.getName().equals("get" + StringUtils.capitalize(excludeProperty))) {
                    skipMethod = true;
                    break;
                }
            }
            if (skipMethod) {
                continue;
            }

            try {
                Object entityFieldValue = method.invoke(entity, null);
                if (entityFieldValue instanceof Collection) {
                    Collection items = (Collection) entityFieldValue;
                    if (CollectionUtils.isNotEmpty(items)) {
                        String propertyClass = null;
                        List<Serializable> ids = Lists.newArrayList();
                        for (Object object : items) {
                            if (object instanceof Persistable) {
                                propertyClass = object.getClass().getSimpleName();
                                ids.add(((Persistable) object).getId());
                            }
                        }
                        if (ids.size() > 0) {
                            entityFieldValue = propertyClass + "[" + StringUtils.join(ids, ",") + "]";
                        }
                    }
                }
                RevEntityProperty item = new RevEntityProperty();
                item.setPropertyName(StringUtils.uncapitalize(StringUtils.substring(method.getName(), 3)));
                item.setPropertyValue(entityFieldValue);
                revEntityProperties.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return revEntityProperties;
    }

    public static class RevEntityProperty {
        private String propertyName;
        private Object propertyValue;

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public Object getPropertyValue() {
            return propertyValue;
        }

        public void setPropertyValue(Object propertyValue) {
            this.propertyValue = propertyValue;
        }

    }
}
