package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.components.UIBean;

/**
 * 扩展标准的checkboxlist标签提供自动化表单数据校验处理支持
 */
public class S2CheckboxListTag extends CheckboxListTag {

    /** 
     * 如果在元素中未定义此属性，则按照属性的类型、JSR303 Validator注解、Hibernate Entity注解等自动组合生成JQuery Validator校验语法字符串
     *  如果在元素中定义此属性则以直接定义属性值作为JQuery Validator校验语法字符串，不再进行自动校验逻辑计算处理 
     */
    protected String validator;

    protected void populateParams() {
        super.populateParams();
        UIBean uiBean = ((UIBean) component);
        TagValidatorAttributeBuilder.buildValidatorAttribute(validator, this, this.getStack(),
                (HttpServletRequest) this.pageContext.getRequest(), uiBean);

        if (this.cssClass == null) {
            uiBean.setCssClass("checkbox inline");
        }
        if (this.theme == null) {
            uiBean.setTheme("bootstrap");
        }
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }
}
