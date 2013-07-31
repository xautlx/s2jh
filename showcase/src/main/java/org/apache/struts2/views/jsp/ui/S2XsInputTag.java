package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.components.UIBean;

public class S2XsInputTag extends TextFieldTag {

    protected String validator;

    protected void populateParams() {
        super.populateParams();
        UIBean uiBean = ((UIBean) component);
        uiBean.setTemplate("xs-input");

        TagValidatorAttributeBuilder.buildValidatorAttribute(validator, this, this.getStack(),
                (HttpServletRequest) this.pageContext.getRequest(), uiBean);

        if (this.cssClass == null) {
            uiBean.setCssClass("input-medium");
        }
        if (this.theme == null) {
            uiBean.setTheme("bootstrap");
        }
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }
}