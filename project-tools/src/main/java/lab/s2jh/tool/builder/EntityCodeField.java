package lab.s2jh.tool.builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 用于代码生成处理的辅助对象
 */
public class EntityCodeField implements Comparable<EntityCodeField>{
    /** 属性标题 */
    private String title;
    /** Java属性名称 */
    private String fieldName;
    /** 属性描述 */
    private String description;
    /** 属性在列表jqGrid中定义的宽度 */
    private Integer listWidth=0;
    /** 在生成代码中属性的相对顺序 */
    private Integer order=Integer.MAX_VALUE;
    /** 属性在列表jqGrid中定义的对齐方式：left，right，center */
    private String listAlign="left";
    /** 属性在列表jqGrid中定义的宽度固定模式 */
    private boolean listFixed = false;
    /** 属性在列表jqGrid中定义的默认不显示模式 */
    private boolean listHidden = false;
    /** 属性在查询区域生成表单元素 */
    private boolean search = false;
    /** 属性在编辑界面生成表单元素 */
    private boolean edit = true;
    /** 属性在jqGrid列表中生成column定义 */
    private boolean list = true;
    /** 标识属性是否枚举类型，根据Java属性反射获取  */
    private Boolean enumField=false;
    /** 属性类型，根据Java属性反射获取 */
    private String fieldType="String";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getListFixed() {
        return listFixed;
    }

    public void setListFixed(boolean listFixed) {
        this.listFixed = listFixed;
    }

    public boolean getListHidden() {
        return listHidden;
    }

    public void setListHidden(boolean listHidden) {
        this.listHidden = listHidden;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public int compareTo(EntityCodeField o) {
        return order.compareTo(o.getOrder());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getListAlign() {
        return listAlign;
    }

    public void setListAlign(String listAlign) {
        this.listAlign = listAlign;
    }

    public Integer getListWidth() {
        return listWidth;
    }

    public void setListWidth(Integer listWidth) {
        this.listWidth = listWidth;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Boolean getEnumField() {
        return enumField;
    }

    public void setEnumField(Boolean enumField) {
        this.enumField = enumField;
    }
    
    public String getUncapitalizeFieldType(){
        return StringUtils.uncapitalize(fieldType);
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }
    

}
