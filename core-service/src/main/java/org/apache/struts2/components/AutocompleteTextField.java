package org.apache.struts2.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import com.opensymphony.xwork2.util.ValueStack;

@StrutsTag(name = "autocompletetextfield", tldTagClass = "org.apache.struts2.views.jsp.ui.S2AutocompleteTextFieldTag", description = "Render an HTML input field of type text", allowDynamicAttributes = true)
public class AutocompleteTextField extends UIBean {
    /**
     * The name of the default template for the TextFieldTag
     */
    final public static String TEMPLATE = "autocompletetext";

    protected String maxlength;
    protected String readonly;
    protected String size;
    protected String source;
    protected String options;
    
    public AutocompleteTextField(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();

        if (size != null) {
            addParameter("size", findString(size));
        }

        if (maxlength != null) {
            addParameter("maxlength", findString(maxlength));
        }

        if (readonly != null) {
            addParameter("readonly", findValue(readonly, Boolean.class));
        }
        
        if (source != null) {
            addParameter("source", findString(source));
        }
        
        if (options != null) {
            addParameter("options", findString(options));
        }
    }

    @StrutsTagAttribute(description = "HTML maxlength attribute", type = "Integer")
    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    @StrutsTagAttribute(description = "Deprecated. Use maxlength instead.", type = "Integer")
    public void setMaxLength(String maxlength) {
        this.maxlength = maxlength;
    }

    @StrutsTagAttribute(description = "Whether the input is readonly", type = "Boolean", defaultValue = "false")
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    @StrutsTagAttribute(description = "HTML size attribute", type = "Integer")
    public void setSize(String size) {
        this.size = size;
    }
    
    @StrutsTagAttribute(description = "HTML source attribute", type = "String")
    public void setSource(String source) {
        this.source = source;
    }
    
    @StrutsTagAttribute(description = "HTML options attribute", type = "String")
    public void setOptions(String options) {
        this.options = options;
    }
}
