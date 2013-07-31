package org.apache.struts2.views.jsp.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.UIBean;

/**
 * 基于日历/时间选取组件封装的日期/时间录入组件
 * 示例：<s2:datetextfield name="search['AD_createdDate']" format="date" cssClass="input-small" />
 */
public class S2DateTextFieldTag extends TextFieldTag {

    /** 
     * 日期格式，支持几个预定义格式属性值：date=yyyy-MM-dd, timestamp=yyyy-MM-dd HH:mm:ss 
     * 其余则按照 @see DateFormat 提供格式字符串即可
     */
    protected String format = "yyyy-MM-dd";

    /**
     * 在通过OGNL未获取到对应的日期值的情况下，是否默认初始化为当前系统时间
     */
    protected Boolean current = Boolean.FALSE;

    /** 
     * 如果在元素中未定义此属性，则按照属性的类型、JSR303 Validator注解、Hibernate Entity注解等自动组合生成JQuery Validator校验语法字符串
     * 如果在元素中定义此属性则以直接定义属性值作为JQuery Validator校验语法字符串，不再进行自动校验逻辑计算处理 
     */
    protected String validator;

    protected void populateParams() {
        super.populateParams();
        UIBean uiBean = ((UIBean) component);
        uiBean.setTemplate("datetext");
        if (id == null) {
            //设置ID随机
            uiBean.setId("datetext_" + RandomStringUtils.randomAlphabetic(10));
        }
        if (this.theme == null) {
            uiBean.setTheme("bootstrap");
        }
        TagValidatorAttributeBuilder.buildValidatorAttribute(validator, this, this.getStack(),
                (HttpServletRequest) this.pageContext.getRequest(), uiBean);

        if (StringUtils.isBlank(format) || "date".equals(format)) {
            format = "yyyy-MM-dd";
        } else if ("timestamp".equals(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        dynamicAttributes.put("format", format);
        uiBean.setDynamicAttributes(dynamicAttributes);

        if (this.cssClass == null) {
            if (format.length() <= 10) {
                uiBean.setCssClass("input-small");
            } else {
                uiBean.setCssClass("input-medium");
            }
        }

        java.util.Date date = null;
        //suport Calendar also
        Object dateObject = null;
        if (value != null) {
            dateObject = findValue(value);
        } else {
            dateObject = findValue(name);
        }
        if (dateObject instanceof java.util.Date)
            date = (java.util.Date) dateObject;
        else if (dateObject instanceof Calendar)
            date = ((Calendar) dateObject).getTime();

        if (date == null && current != null && current == true) {
            date = new java.util.Date();
        }

        if (date != null) {
            String dateLabel = new SimpleDateFormat(format).format(date);
            uiBean.setValue(dateLabel);
        }
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }
}
