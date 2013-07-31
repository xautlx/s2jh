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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.struts2.components.UIBean;


/**
 * 提供一个可展开收拢效果的select组件，实现灵活的单一或多选选取
 * 主要用在查询界面，默认按照单选下拉框显示单一条件查询，
 * 用户也可点击右侧展开成多选形式方便选择多选项OR查询
 * 示例：<s2:multiselect cssClass="input-medium" name="search['IN_type']" list="#application.menuTypeEnumMap" />
 */
public class S2MultiSelectTag extends SelectTag {

    protected void populateParams() {
        super.populateParams();
        UIBean uiBean = ((UIBean) component);
        uiBean.setTemplate("multiselect");
        if (id == null) {
            uiBean.setId("select_" + RandomStringUtils.randomAlphabetic(10));
        }
        if (this.theme == null) {
            uiBean.setTheme("bootstrap");
        }
    }
}
