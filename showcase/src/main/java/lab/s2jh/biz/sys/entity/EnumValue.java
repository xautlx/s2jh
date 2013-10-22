package lab.s2jh.biz.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "SYS_ENUM_VALUE", uniqueConstraints = { @UniqueConstraint(columnNames = { "enumType", "code" }) })
@MetaData(value = "数据字典内容")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class EnumValue extends PersistableEntity<String> {

    @MetaData(value = "枚举类型", description = "填写代码表的表名，如：XX_JBXX")
    @EntityAutoCode(order = 10, search = true)
    private String enumType;

    @MetaData(value = "枚举值")
    @EntityAutoCode(order = 15, search = true)
    private String code;

    @MetaData(value = "描述", description = "填写代码表的中文描述，如：学校类别")
    @EntityAutoCode(order = 20, search = true)
    private String label;

    @MetaData(value = "父节点")
    @EntityAutoCode(order = 35, search = false)
    private String parentCode;

    @MetaData(value = "数据顺序", description = "1.国家标准  2.教育部标准  3.系统定义  4.用户定义")
    @EntityAutoCode(order = 40, search = true)
    private Integer codeOrder;

    @MetaData(value = "是否可修改", description = "用户是否可以修改，1-可以， 2-不可以")
    @EntityAutoCode(order = 50, searchAdvance = true)
    private Boolean fixed = Boolean.FALSE;

    @MetaData(value = "是否显示", description = "前台下拉框是否显示，1-显示， 2-不显示")
    @EntityAutoCode(order = 60, searchAdvance = true)
    private Boolean display = Boolean.FALSE;

    @MetaData(value = "可用状态", description = "1：可用  0：不可用")
    @EntityAutoCode(order = 70, search = true)
    private Boolean enabled = Boolean.TRUE;

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

    @Type(type = "lab.s2jh.biz.core.hib.BooleanStrUserType")
    @Column(nullable = false, length = 1, name = "status")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    @Transient
    public String getDisplayLabel() {
        return enumType;
    }

    @Column(nullable = false, length = 60)
    public String getEnumType() {
        return enumType;
    }

    public void setEnumType(String enumType) {
        this.enumType = enumType;
    }

    @Column(nullable = false, length = 200, name = "description")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Column(nullable = false, length = 40)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(nullable = true, length = 40)
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getCodeOrder() {
        return codeOrder;
    }

    public void setCodeOrder(Integer codeOrder) {
        this.codeOrder = codeOrder;
    }

    @Type(type = "lab.s2jh.biz.core.hib.Boolean1T2FUserType")
    @Column(nullable = true, length = 1, name = "IS_FIXED")
    public Boolean getFixed() {
        return fixed;
    }

    public void setFixed(Boolean fixed) {
        this.fixed = fixed;
    }

    @Type(type = "lab.s2jh.biz.core.hib.Boolean1T2FUserType")
    @Column(nullable = true, length = 1, name = "IS_DISPLAY")
    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }
}
