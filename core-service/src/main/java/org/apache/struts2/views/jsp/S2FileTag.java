package org.apache.struts2.views.jsp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.struts2.components.Component;
import org.apache.struts2.components.File;

import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/**
 * 生成基于自定义的附件文件对象的显示和下载链接
 * 示例：<s2:file value="r2File" label="关联附件"/>
 */
public class S2FileTag extends ComponentTagSupport {

    private static final Logger LOG = LoggerFactory.getLogger(S2FileTag.class);

    /** Bootstrap样式label属性定义 */
    private String label;

    /** 返回附件对象实例   @see File.FileDef  */
    private String value;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new File(stack);
    }

    protected void populateParams() {
        super.populateParams();
        File uiBean = ((File) component);
        uiBean.setValue(value);
    }

    public int doEndTag() throws JspException {
        JspWriter writer = pageContext.getOut();
        if (label != null) {
            try {
                writer.write("<div class='control-group'><label class='control-label'>");
                writer.write(label);
                writer.write("</label><div class='controls'>");
            } catch (IOException e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Could not print out value '" + label + "'", e);
                }
            }
        }
        component.end(writer, getBody());
        if (label != null) {
            try {
                writer.write("</div></div>");
            } catch (IOException e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Could not print out value '" + label + "'", e);
                }
            }
        }
        component = null;
        return EVAL_PAGE;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
