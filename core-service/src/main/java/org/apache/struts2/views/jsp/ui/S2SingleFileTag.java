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
import org.apache.struts2.components.TextField;

/**
 * 单文件上传关联组件
 * 基本原理：基于JQuery File Upload插件，选取文件后自动请求/sys/attachment-file!uploadSingle上传当前附件，
 * 之后把创建的AttachmentFile数据的主键和文件名称信息带回表单输入元素，业务功能Controller代码需要自行处理回传的AttachmentFile对象关联逻辑
 * 示例：<s2:singlefile name="templateFileId" labelValue="%{templateFile.fileRealName}" label="附件" value="%{templateFile.id}" />
 */
public class S2SingleFileTag extends TextFieldTag {

    /** 
     * 如果在元素中未定义此属性，则按照属性的类型、JSR303 Validator注解、Hibernate Entity注解等自动组合生成JQuery Validator校验语法字符串
     * 如果在元素中定义此属性则以直接定义属性值作为JQuery Validator校验语法字符串，不再进行自动校验逻辑计算处理 
     */
    protected String validator;

    /** 附件名称显示字面值 */
    protected String labelValue;

    protected void populateParams() {
        //此段落必须放在super.populateParams()之前
        try {
            this.setDynamicAttribute(null, "hiddenName", this.name);

            Object realHiddenValue = null;
            if (this.value != null) {
                realHiddenValue = findValue(this.value);
            } else if (this.name != null) {
                realHiddenValue = findValue(this.name);
            }
            if (realHiddenValue != null) {
                this.setDynamicAttribute(null, "hiddenValue", realHiddenValue);
            }
        } catch (JspException e) {
            e.printStackTrace();
        }

        super.populateParams();

        TextField uiBean = ((TextField) component);
        uiBean.setTemplate("singlefile");
        if (id == null) {
            //设置ID随机
            uiBean.setId("file_" + RandomStringUtils.randomAlphabetic(10));
        }
        
        uiBean.setName(this.name + "_label");
        uiBean.setValue(labelValue);

        if (this.cssClass == null) {
            uiBean.setCssClass("input-large");
        }
        if (this.theme == null) {
            uiBean.setTheme("bootstrap");
        }
        if (this.readonly == null) {
            uiBean.setReadonly("true");
        }

        TagValidatorAttributeBuilder.buildValidatorAttribute(validator, this, this.getStack(),
                (HttpServletRequest) this.pageContext.getRequest(), uiBean);
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }
}
