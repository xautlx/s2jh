package org.apache.struts2.views.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.struts2.components.Date;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/**
 * 扩展标准的s:date标签，生成Bootstrap样式的日期显示
 * 在format标准支持上添加date和timestamp两种内置转换处理
 */
public class S2DateTag extends DateTag {
    
    private static final Logger LOG = LoggerFactory.getLogger(S2DateTag.class);
    
    /** Bootstrap样式label属性定义 */
    private String label;

    protected void populateParams() {
        
        super.populateParams();
        Date uiBean = ((Date) component);

        if (this.format == null || this.format.equals("date")) {
            uiBean.setFormat("yyyy-MM-dd");
        } else if (this.format.equals("timestamp")) {
            uiBean.setFormat("yyyy-MM-dd HH:mm:ss");
        }
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
}
