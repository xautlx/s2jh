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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.Component;
import org.apache.struts2.components.S2RegionSelect;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 行政区划选取组件
 * @see ComboSelect
 */
public class S2RegionSelectTag extends SelectTag {

    protected String validator;

    
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new S2RegionSelect(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();
        S2RegionSelect uiBean = ((S2RegionSelect) component);

        if (StringUtils.isBlank(emptyOption)) {
            uiBean.setEmptyOption("true");
        }
        if (this.cssClass == null) {
            uiBean.setCssClass("input-medium");
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

}
