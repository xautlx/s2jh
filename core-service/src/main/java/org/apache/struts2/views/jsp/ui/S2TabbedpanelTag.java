package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.Component;
import org.apache.struts2.components.Div;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 基于JQuery UI Tabs组件的标签封装
 * 示例：<s2:tabbedpanel id="privilegeIndexTabs"><ul><li>..</li></ul><div>...</div></s2:tabbedpanel>
 */
public class S2TabbedpanelTag extends AbstractClosingTag {

    /**
     * 是否把除第一个Tab以外的其他初始化为disabled
     * 主要用于在数据创建界面，一般默认基本信息编辑Tab可操作，
     * 其他则disabled待第一个基本信息编辑操作完成后刷新页面使其他Tab界面可编辑
     */
    protected String disableItemsExcludeFirst;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Div(stack, req, res);
    }

    protected void populateParams() {

        try {
            if (StringUtils.isNotBlank(disableItemsExcludeFirst)) {
                this.setDynamicAttribute(null, "disableItemsExcludeFirst", disableItemsExcludeFirst);
            }
        } catch (JspException e) {
            e.printStackTrace();
        }

        super.populateParams();
        Div uiBean = ((Div) component);
        uiBean.setTemplate("tabbedpanel-close");
        uiBean.setOpenTemplate("tabbedpanel");
        if (id == null) {
            //设置ID随机
            uiBean.setId("tabbedpanel_" + RandomStringUtils.randomAlphabetic(10));
        }
        if (this.cssClass == null) {
            uiBean.setCssClass("hide");
        }
        if (this.theme == null) {
            uiBean.setTheme("bootstrap");
        }
    }

    public void setDisableItemsExcludeFirst(String disableItemsExcludeFirst) {
        this.disableItemsExcludeFirst = disableItemsExcludeFirst;
    }
}