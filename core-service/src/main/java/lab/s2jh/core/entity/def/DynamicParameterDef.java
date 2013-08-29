package lab.s2jh.core.entity.def;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

@MappedSuperclass
public abstract class DynamicParameterDef extends BaseEntity<String> {

    @MetaData(title = "代码")
    @EntityAutoCode(order = 10, search = true)
    private String code;

    @MetaData(title = "名称")
    @EntityAutoCode(order = 20, search = true)
    private String title;

    @MetaData(title = "描述")
    @EntityAutoCode(listShow = false)
    private String description;

    @MetaData(title = "必填标识")
    @EntityAutoCode(order = 30, search = true)
    private Boolean required = Boolean.FALSE;

    @MetaData(title = "禁用标识", description = "禁用项全局不显示")
    @EntityAutoCode(order = 40, search = true)
    private Boolean disabled = Boolean.FALSE;
    
    @MetaData(title = "隐藏标识", description = "隐藏项目不用用户输入，一般需要配置合理的defaultValue")
    @EntityAutoCode(order = 45, search = true)
    private Boolean hidden = Boolean.FALSE;

    @MetaData(title = "参数类型")
    @EntityAutoCode(order = 50, search = true)
    private DynamicParameterTypeEnum type = DynamicParameterTypeEnum.STRING;

    @MetaData(title = "前端UI校验规则", description = "如：{required:true,min:0,max:1000}")
    @EntityAutoCode(order = 55, search = false)
    private String validateRules;

    @MetaData(title = "缺省参数值")
    @EntityAutoCode(order = 60, search = false)
    private String defaultValue;

    @MetaData(title = "是否允许多选 ", description = "用于下拉框数据参数")
    @EntityAutoCode(order = 70, search = false)
    private Boolean multiSelectFlag = Boolean.FALSE;

    @MetaData(title = "集合数据源 ", description = "对于List类型数据的数据源指定，即定义如何提供给用户选取的数据 ")
    @EntityAutoCode(order = 70, search = false)
    private String listDataSource;

    @MetaData(title = "排序号", description = "相对排序号，数字越大越靠上显示")
    @EntityAutoCode(order = 1000)
    private Integer orderRank = 100;

    @Column(nullable = false, length = 64)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 32, nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 512, nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
    
    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getOrderRank() {
        return orderRank;
    }

    public void setOrderRank(Integer orderRank) {
        this.orderRank = orderRank;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = true)
    public DynamicParameterTypeEnum getType() {
        return type;
    }

    public void setType(DynamicParameterTypeEnum type) {
        this.type = type;
    }

    @Column(length = 256, nullable = true)
    public String getValidateRules() {
        return validateRules;
    }

    public void setValidateRules(String validateRules) {
        this.validateRules = validateRules;
    }

    @Column(length = 4000, nullable = true)
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getMultiSelectFlag() {
        return multiSelectFlag;
    }

    public void setMultiSelectFlag(Boolean multiSelectFlag) {
        this.multiSelectFlag = multiSelectFlag;
    }

    @Column(length = 512, nullable = true)
    public String getListDataSource() {
        return listDataSource;
    }

    public void setListDataSource(String listDataSource) {
        this.listDataSource = listDataSource;
    }

    @Transient
    public String getFullValidateRules() {
        StringBuilder sb = new StringBuilder();
        switch (this.getType()) {
        case DATE:
            sb.append("date:true,");
            break;
        case TIMESTAMP:
            sb.append("timestamp:true,");
            break;
        case FLOAT:
            sb.append("number:true,");
            break;
        case INTEGER:
            sb.append("number:true,digits:true,");
            break;
        default:
        }
        if (BooleanUtils.toBoolean(this.required)) {
            sb.append("required:true,");
        }
        if (StringUtils.isNotBlank(this.getValidateRules())) {
            sb.append(this.getValidateRules() + ",");
        }
        if (sb.length() == 0) {
            return "";
        } else {
            return "{" + sb.substring(0, sb.length() - 1) + "}";
        }
    }



}
