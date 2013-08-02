package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.UIBean;

/**
 * 基于JQuery UI AutoComplete封装自动输入提示完成组件
 */
public class S2AutocompleteTextFieldTag extends TextFieldTag {

    /** JQuery UI AutoComplete对应属性: 触发AJAX数据请求的最小字符长度 */
    protected String minLength;
    /** JQuery UI AutoComplete对应属性: 触发AJAX数据请求的延迟 */
    protected String delay;
    /** JQuery UI AutoComplete对应属性: AJAX数据请求URL */
    protected String source;
    /** 初始化显示的字面值 */
    protected String labelValue;

    /** 
     * 如果在元素中未定义此属性，则按照属性的类型、JSR303 Validator注解、Hibernate Entity注解等自动组合生成JQuery Validator校验语法字符串
     * 如果在元素中定义此属性则以直接定义属性值作为JQuery Validator校验语法字符串，不再进行自动校验逻辑计算处理 
     */
    protected String validator;

    protected void populateParams() {
        try {
            this.setDynamicAttribute(null, "source", source);
            if (StringUtils.isNotBlank(minLength)) {
                this.setDynamicAttribute(null, "delay", delay);
            }
            if (StringUtils.isNotBlank(minLength)) {
                this.setDynamicAttribute(null, "minLength", minLength);
            }
            Object realLabelValue = null;
            if (this.labelValue != null) {
                realLabelValue = findValue(this.labelValue);
            }
            if (realLabelValue != null) {
                this.setDynamicAttribute(null, "labelValue", realLabelValue);
            }
        } catch (JspException e) {
            e.printStackTrace();
        }

        super.populateParams();

        UIBean uiBean = ((UIBean) component);
        uiBean.setTemplate("autocompletetext");
        if (id == null) {
            //设置ID随机
            uiBean.setId("autocompletetext_" + RandomStringUtils.randomAlphabetic(10));
        }
        if (this.theme == null) {
            uiBean.setTheme("bootstrap");
        }
        TagValidatorAttributeBuilder.buildValidatorAttribute(validator, this, this.getStack(),
                (HttpServletRequest) this.pageContext.getRequest(), uiBean);
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

}