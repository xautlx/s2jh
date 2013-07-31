package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.struts2.components.UIBean;

public class S2AclCodeTag extends TextFieldTag {

    protected String validator;

    protected void populateParams() {
        super.populateParams();
        UIBean uiBean = ((UIBean) component);
        uiBean.setTemplate("aclcode");

        if (id == null) {
            //设置ID随机
            uiBean.setId("aclcode_" + RandomStringUtils.randomAlphabetic(10));
        }

        TagValidatorAttributeBuilder.buildValidatorAttribute(validator, this, this.getStack(),
                (HttpServletRequest) this.pageContext.getRequest(), uiBean);

        if (this.cssClass == null) {
            uiBean.setCssClass("input-medium");
        }

    }

    public void setValidator(String validator) {
        this.validator = validator;
    }
}
