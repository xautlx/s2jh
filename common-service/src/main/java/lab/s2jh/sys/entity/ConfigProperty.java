package lab.s2jh.sys.entity;

import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.cfg.DynamicPropertyPlaceholderConfigurer;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "T_SYS_CFG_PROP")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(title = "配置属性")
public class ConfigProperty extends BaseEntity<String> {

    @MetaData(title = "代码")
    @EntityAutoCode(order = 10)
    private String propKey;

    @MetaData(title = "名称")
    @EntityAutoCode(order = 20)
    private String propName;

    @MetaData(title = "简单属性值")
    @EntityAutoCode(order = 30)
    private String simpleValue;

    @MetaData(title = "HTML属性值")
    @EntityAutoCode(order = 40)
    private String htmlValue;

    @MetaData(title = "参数属性用法说明")
    @EntityAutoCode(order = 50)
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

    @Column(length = 64, unique = true)
    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey;
    }

    @Column(length = 256)
    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    @JsonIgnore
    @Column(length = 2000)
    public String getPropDescn() {
        return propDescn;
    }

    public void setPropDescn(String propDescn) {
        this.propDescn = propDescn;
    }

    @Column(length = 256)
    public String getSimpleValue() {
        return simpleValue;
    }

    public void setSimpleValue(String simpleValue) {
        this.simpleValue = simpleValue;
    }

    @Lob
    @JsonIgnore
    public String getHtmlValue() {
        return htmlValue;
    }

    public void setHtmlValue(String htmlValue) {
        this.htmlValue = htmlValue;
    }

    @Transient
    public String getStaticConfigValue() {
        Properties properties = DynamicPropertyPlaceholderConfigurer.getPropertiesContainer();
        return properties.getProperty(propKey);
    }
}
