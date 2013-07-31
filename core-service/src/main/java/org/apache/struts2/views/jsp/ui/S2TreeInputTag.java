/*
 * $Id: SelectTag.java 651946 2008-04-27 13:41:38Z apetrelli $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.UIBean;

/**
 * 基于下拉输入框样式的树形结构数据选取组件
 * 示例：<s2:treeinput name="parentName" label="父节点" hiddenName="parentId" 
           treeDataUrl="/sys/menu!list" readonly="true" value="%{parent.title}" hiddenValue="%{parent.id}" />
 */
public class S2TreeInputTag extends TextFieldTag {

    /** 
     * 如果在元素中未定义此属性，则按照属性的类型、JSR303 Validator注解、Hibernate Entity注解等自动组合生成JQuery Validator校验语法字符串
     * 如果在元素中定义此属性则以直接定义属性值作为JQuery Validator校验语法字符串，不再进行自动校验逻辑计算处理 
     */
    protected String validator;

    /** 展开树形数据选取界面时AJAX异步加载Tree结构数据的URL */
    protected String treeDataUrl;

    /** 默认选取文本带入输入框显示，节点对应底层的Value值以隐藏元素hiddenName存取 */
    protected String hiddenName;

    /** hiddenName隐藏元素初始化值 */
    protected String hiddenValue;

    protected void populateParams() {
        //此段落必须放在super.populateParams()之前
        try {
            if (StringUtils.isNotBlank(treeDataUrl)) {
                this.setDynamicAttribute(null, "treeDataUrl", treeDataUrl);
            }
            if (StringUtils.isNotBlank(hiddenName)) {
                this.setDynamicAttribute(null, "hiddenName", hiddenName);
            }

            Object realHiddenValue = null;
            if (hiddenValue != null) {
                realHiddenValue = findValue(hiddenValue);
            } else if (hiddenName != null) {
                realHiddenValue = findValue(hiddenName);
            }
            if (realHiddenValue != null) {
                this.setDynamicAttribute(null, "hiddenValue", realHiddenValue);
            }
        } catch (JspException e) {
            e.printStackTrace();
        }

        super.populateParams();

        UIBean uiBean = ((UIBean) component);
        uiBean.setTemplate("treeinput");
        if (id == null) {
            //设置ID随机
            uiBean.setId("treeinput_" + RandomStringUtils.randomAlphabetic(10));
        }

        if (this.cssClass == null) {
            uiBean.setCssClass("input-fluid");
        }
        if (this.theme == null) {
            uiBean.setTheme("bootstrap");
        }

        TagValidatorAttributeBuilder.buildValidatorAttribute(validator, this, this.getStack(),
                (HttpServletRequest) this.pageContext.getRequest(), uiBean);
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public void setTreeDataUrl(String treeDataUrl) {
        this.treeDataUrl = treeDataUrl;
    }

    public void setHiddenName(String hiddenName) {
        this.hiddenName = hiddenName;
    }

    public void setHiddenValue(String hiddenValue) {
        this.hiddenValue = hiddenValue;
    }
}
