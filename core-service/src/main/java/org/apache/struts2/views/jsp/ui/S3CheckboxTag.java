package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.components.UIBean;

/**
 * 扩展标准的checkboxlist标签提供自动化表单数据校验处理支持
 */
public class S3CheckboxTag extends CheckboxTag {

    protected void populateParams() {
        super.populateParams();
        UIBean uiBean = ((UIBean) component);
        S3TagValidationBuilder.build(this, this.getStack(), (HttpServletRequest) this.pageContext.getRequest(), uiBean);

        if (this.theme == null) {
            uiBean.setTheme("bootstrap3");
        }
    }
}
