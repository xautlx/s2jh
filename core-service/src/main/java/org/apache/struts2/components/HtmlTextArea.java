package org.apache.struts2.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import com.opensymphony.xwork2.util.ValueStack;

@StrutsTag(
        name="htmltextarea",
        tldTagClass="org.apache.struts2.views.jsp.ui.S2HtmlTextareaTag",
        description="Render HTML textarea tag.",
        allowDynamicAttributes=true)
public class HtmlTextArea extends UIBean {
    final public static String TEMPLATE = "htmltextarea";

    protected String cols;
    protected String readonly;
    protected String rows;
    protected String wrap;
    protected String height;

    public HtmlTextArea(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    public void evaluateExtraParams() {
        super.evaluateExtraParams();

        if (readonly != null) {
            addParameter("readonly", findValue(readonly, Boolean.class));
        }

        if (cols != null) {
            addParameter("cols", findString(cols));
        }

        if (rows != null) {
            addParameter("rows", findString(rows));
        }

        if (wrap != null) {
            addParameter("wrap", findString(wrap));
        }
        
        if (height != null) {
            addParameter("height", findString(height));
        }
    }

    @StrutsTagAttribute(description="HTML cols attribute", type="Integer")
    public void setCols(String cols) {
        this.cols = cols;
    }

    @StrutsTagAttribute(description="Whether the textarea is readonly", type="Boolean", defaultValue="false")
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    @StrutsTagAttribute(description="HTML rows attribute", type="Integer")
    public void setRows(String rows) {
        this.rows = rows;
    }

    @StrutsTagAttribute(description="HTML wrap attribute")
    public void setWrap(String wrap) {
        this.wrap = wrap;
    }

    @StrutsTagAttribute(description="HTML height attribute")
    public void setHeight(String height) {
        this.height = height;
    }

}
