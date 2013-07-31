package lab.s2jh.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_SYS_CFG_PROP")
@Cache(usage = CacheConcurrencyStrategy.NONE)
@MetaData(title = "配置属性")
public class ConfigProperty extends BaseEntity<String> {

    private String propKey;
    
    private String propName;

    private String propStaticValue;
    
    private String propDynamicValue;
    
    private String propDescn;

    private String id;

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    @Transient
    public String getDisplayLabel() {
        return propKey;
    }

    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropStaticValue() {
        return propStaticValue;
    }

    public void setPropStaticValue(String propStaticValue) {
        this.propStaticValue = propStaticValue;
    }

    public String getPropDynamicValue() {
        return propDynamicValue;
    }

    public void setPropDynamicValue(String propDynamicValue) {
        this.propDynamicValue = propDynamicValue;
    }

    public String getPropDescn() {
        return propDescn;
    }

    public void setPropDescn(String propDescn) {
        this.propDescn = propDescn;
    }
}
