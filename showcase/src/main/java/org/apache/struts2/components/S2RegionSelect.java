/*
 * $Id: Select.java 651946 2008-04-27 13:41:38Z apetrelli $
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

package org.apache.struts2.components;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.views.annotations.StrutsTag;

import com.opensymphony.xwork2.util.ValueStack;

@StrutsTag(name = "regionselect", tldTagClass = "org.apache.struts2.views.jsp.ui.S2RegionSelectTag", description = "Render a multi select element", allowDynamicAttributes = true)
public class S2RegionSelect extends Select {

    final public static String TEMPLATE = "regionselect";

    public S2RegionSelect(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    public void evaluateExtraParams() {
        super.evaluateExtraParams();
        
        String id=(String)parameters.get("id");
        id=id.replaceAll("[\\$\\']", "_");
        addParameter("id",id);

        String name = (String) parameters.get("name");
        String nameValue = (String) parameters.get("nameValue");

        addParameter("provinceList", findValue("%{findProvinceRegions()}", Map.class));

        addParameter("provinceName", name + "_province");
        addParameter("cityName", name + "_city");
        addParameter("provinceId", id + "_province");
        addParameter("cityId", id + "_city");

        if (StringUtils.isNotBlank(nameValue)) {
            addParameter("cityList", findValue("%{findCityRegions('" + nameValue + "')}", Map.class));
            addParameter("list", findValue("%{findDistrictRegions('" + nameValue + "')}", Map.class));
            addParameter("provinceValue", StringUtils.substring(nameValue, 0, 2) + "0000");
            addParameter("cityValue", StringUtils.substring(nameValue, 0, 4) + "00");
        }

    }
}
