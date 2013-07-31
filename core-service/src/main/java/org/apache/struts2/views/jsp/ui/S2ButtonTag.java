package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.Button;
import org.apache.struts2.components.Component;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * HTML Button元素封装，主要方便JSP页面基于OGNL的disabled属性控制按钮状态
 * 用法示例：
 * <s2:button cssClass="btn btn-submit submit-post-close" disabled="disallowAudit">
 *    <i class="icon-check"></i> 提交审批
 * </s2:button>
 */
public class S2ButtonTag extends AbstractClosingTag {

    /** 基于HTML Button元素的type类型属性定义：button、reset、submit */
    protected String type;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Button(stack, req, res);
    }

    protected void populateParams() {
        try {
            if (StringUtils.isNotBlank(type)) {
                this.setDynamicAttribute(null, "type", type);
            }
        } catch (JspException e) {
            e.printStackTrace();
        }

        super.populateParams();

        Button uiBean = ((Button) component);
        if (id == null) {
            //设置ID随机
            uiBean.setId("button_" + RandomStringUtils.randomAlphabetic(10));
        }
        if (theme == null) {
            uiBean.setTheme("bootstrap");
        }
    }

    public void setType(String type) {
        this.type = type;
    }
}