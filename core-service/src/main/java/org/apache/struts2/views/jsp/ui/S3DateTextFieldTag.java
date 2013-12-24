package org.apache.struts2.views.jsp.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.UIBean;

/**
 * 基于日历/时间选取组件封装的日期/时间录入组件
 * 示例：<s2:datetextfield name="search['AD_createdDate']" format="date" cssClass="input-small" />
 */
public class S3DateTextFieldTag extends TextFieldTag {

    /** 
     * 日期格式，支持几个预定义格式属性值：date=yyyy-MM-dd, timestamp=yyyy-MM-dd HH:mm:ss 
     * 其余则按照 @see DateFormat 提供格式字符串即可
     */
    protected String format = "yyyy-MM-dd";

    /**
     * 在通过OGNL未获取到对应的日期值的情况下，是否默认初始化为当前系统时间
     */
    protected Boolean current = Boolean.FALSE;

    protected void populateParams() {
        super.populateParams();
        UIBean uiBean = ((UIBean) component);
        uiBean.setTemplate("datetext");

        S3TagValidationBuilder.build(this, this.getStack(), (HttpServletRequest) this.pageContext.getRequest(), uiBean);
        if (this.cssClass == null) {
            uiBean.setCssClass("form-control");
        }
        if (this.theme == null) {
            uiBean.setTheme("bootstrap3");
        }
        if (StringUtils.isBlank(format) || "date".equals(format)) {
            format = "yyyy-MM-dd";
        } else if ("timestamp".equals(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        dynamicAttributes.put("format", format);
        uiBean.setDynamicAttributes(dynamicAttributes);

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

    public void setCurrent(Boolean current) {
        this.current = current;
    }
}
