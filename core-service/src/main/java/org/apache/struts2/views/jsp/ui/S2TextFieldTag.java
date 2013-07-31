/*
 * $Id: TextFieldTag.java 1389534 2012-09-24 19:43:44Z jogep $
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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.struts2.components.Component;
import org.apache.struts2.components.TextField;
import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 扩展标准的textfield标签，提供自动化数据校验处理支持
 * 及其他如样式等默认值定义
 */
public class S2TextFieldTag extends TextFieldTag {

    /** 
     * 如果在元素中未定义此属性，则按照属性的类型、JSR303 Validator注解、Hibernate Entity注解等自动组合生成JQuery Validator校验语法字符串
     * 如果在元素中定义此属性则以直接定义属性值作为JQuery Validator校验语法字符串，不再进行自动校验逻辑计算处理 
     */
    protected String validator;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new TextField(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();
        UIBean uiBean = ((UIBean) component);
        TagValidatorAttributeBuilder.buildValidatorAttribute(validator, this, this.getStack(),
                (HttpServletRequest) this.pageContext.getRequest(), uiBean);
        if (this.cssClass == null) {
            uiBean.setCssClass("input-fluid");
        }
        if (this.theme == null) {
            uiBean.setTheme("bootstrap");
        }
        if (this.id == null) {
            uiBean.setId("text_" + RandomStringUtils.randomAlphabetic(10));
        }
        
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }
}
