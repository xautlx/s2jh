package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.components.TextArea;
import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 扩展标准的textarea标签，提供自动化数据校验处理支持
 * 及其他如样式等默认值定义
 */
public class S2TextareaTag extends TextareaTag {
    
    /** 
     * 如果在元素中未定义此属性，则按照属性的类型、JSR303 Validator注解、Hibernate Entity注解等自动组合生成JQuery Validator校验语法字符串
     * 如果在元素中定义此属性则以直接定义属性值作为JQuery Validator校验语法字符串，不再进行自动校验逻辑计算处理 
     */
    protected String validator;


    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new TextArea(stack, req, res);
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
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }
}
