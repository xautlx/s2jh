package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.components.HtmlTextArea;

import com.opensymphony.xwork2.util.ValueStack;

public class S2HtmlTextareaTag extends AbstractUITag {

    protected String cols;
    protected String readonly;
    protected String rows;
    protected String wrap;
    protected String height;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new HtmlTextArea(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();

        HtmlTextArea textArea = ((HtmlTextArea) component);
        textArea.setCols(cols);
        textArea.setReadonly(readonly);
        textArea.setRows(rows);
        textArea.setWrap(wrap);
        textArea.setHeight(height);
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public void setWrap(String wrap) {
        this.wrap = wrap;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
