package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.components.UIBean;

/**
 * 扩展标准的textarea标签，提供自动化数据校验处理支持
 * 及其他如样式等默认值定义
 */
public class S3TextareaTag extends TextareaTag {
    
    protected void populateParams() {
        super.populateParams();
        UIBean uiBean = ((UIBean) component);
        S3TagValidationBuilder.build(this, this.getStack(), (HttpServletRequest) this.pageContext.getRequest(), uiBean);
        
        if (this.cssClass == null) {
            uiBean.setCssClass("form-control");
        }
        if (this.theme == null) {
            uiBean.setTheme("bootstrap3");
        }
    }
}
