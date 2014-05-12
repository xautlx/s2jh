package org.apache.struts2.views.jsp.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.UIBean;
import org.joda.time.DateTime;

/**
 * 基于日历/时间选取组件封装的日期/时间录入组件
 * 示例：<s2:datetextfield name="search['AD_createdDate']" format="date"/>
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

    /**
     * 基于当前日期的一个偏差值，格式示例：+30d:当前30天之后，-1m：当前一个月之前，+1y: 当前1年之后
     */
    protected String offset;

    protected void populateParams() {
        super.populateParams();
        UIBean uiBean = ((UIBean) component);
        uiBean.setTemplate("datetext");

        if (this.theme == null) {
            uiBean.setTheme("bootstrap3");
        }
        if (this.cssClass == null) {
            uiBean.setCssClass("form-control");
        }
        if (StringUtils.isBlank(format) || "date".equals(format)) {
            format = "yyyy-MM-dd";
            if (this.cssClass == null) {
                uiBean.setCssClass("form-control");
            }
        } else if ("timestamp".equals(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        //        if (this.cssClass == null) {
        //            if (format.length() > 10) {
        //                dynamicAttributes.put("dateInputClass", "input-xlarge");
        //            } else {
        //                dynamicAttributes.put("dateInputClass", "input-medium");
        //            }
        //        }
        dynamicAttributes.put("dateInputClass", "");

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

        if (date == null) {
            if (current != null && current == true) {
                date = new java.util.Date();
            } else if (StringUtils.isNotBlank(offset)) {
                offset = offset.toLowerCase();
                boolean plus = true;
                String type = "d";
                String value = offset;
                if (offset.startsWith("-")) {
                    plus = false;
                    value = StringUtils.substringAfter(value, "-");
                } else if (offset.startsWith("+")) {
                    value = StringUtils.substringAfter(value, "+");
                }
                if (value.endsWith("d")) {
                    type = "d";
                    value = StringUtils.substringBeforeLast(value, "d");
                } else if (value.endsWith("m")) {
                    type = "m";
                    value = StringUtils.substringBeforeLast(value, "m");
                } else if (value.endsWith("y")) {
                    type = "y";
                    value = StringUtils.substringBeforeLast(value, "y");
                }
                int ov = Integer.valueOf(value);
                if (!plus) {
                    ov = -ov;
                }
                if (type.equals("d")) {
                    date = new DateTime().plusDays(ov).toDate();
                } else if (type.equals("m")) {
                    date = new DateTime().plusMonths(ov).toDate();
                } else if (type.equals("y")) {
                    date = new DateTime().plusYears(ov).toDate();
                }
            }
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

    public void setOffset(String offset) {
        this.offset = offset;
    }
}
