package org.apache.struts2.views.jsp.ui;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.TextArea;

/**
 * 基于KindEditor封装的大文本录入组件标签
 * 用法示例：<s2:kindeditor name="description" label="描述" rows="3"/>
 */
public class S2KindEditorTag extends TextareaTag {

    /**
     * 配置编辑器的工具栏，其中”/”表示换行，”|”表示分隔符。
     * http://www.kindsoft.net/docs/option.html#items
     * 未提供参数取组件默认配置项，内置“simple”表示简单配置项，其余可按照组件文档提供配置项定义
     */
    protected String items;

    protected void populateParams() {

        super.populateParams();

        TextArea uiBean = ((TextArea) component);
        uiBean.setTemplate("kindeditor");
        if (id == null) {
            //设置ID随机
            uiBean.setId("kindeditor_" + RandomStringUtils.randomAlphabetic(10));
        }
        if (this.theme == null) {
            uiBean.setTheme("bootstrap");
        }

        if (StringUtils.isNotBlank(items)) {
            if ("simple".equals(items)) {
                items = "['fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',"
                        + "'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',"
                        + "'insertunorderedlist', '|', 'emoticons', 'image', 'link']";
            }
            dynamicAttributes.put("items", items);
        }
    }

    public void setItems(String items) {
        this.items = items;
    }
}
