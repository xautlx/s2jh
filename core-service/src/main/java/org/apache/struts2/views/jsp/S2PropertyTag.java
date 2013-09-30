package org.apache.struts2.views.jsp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.struts2.components.Property;
import org.apache.struts2.dispatcher.Dispatcher;

import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/**
 * 扩展标准的s:property标签，生成Bootstrap样式的属性数据显示
 */
public class S2PropertyTag extends PropertyTag {

    private static final Logger LOG = LoggerFactory.getLogger(S2PropertyTag.class);
    
    /** 以HTML pre格式化方式显示内容,如显示异常堆栈文本 */
    private String pre;

    /** Bootstrap样式label属性定义 */
    private String label;

    protected void populateParams() {
        super.populateParams();
        Property uiBean = ((Property) component);
    }

    public int doStartTag() throws JspException {
        component = getBean(getStack(), (HttpServletRequest) pageContext.getRequest(),
                (HttpServletResponse) pageContext.getResponse());
        Container container = Dispatcher.getInstance().getContainer();
        container.inject(component);

        populateParams();

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
        if("true".equalsIgnoreCase(pre)){
            try {
                writer.write("<pre>");
            } catch (IOException e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Could not print out pre tag", e);
                }
            }
        }
        boolean evalBody = component.start(writer);
        if("true".equalsIgnoreCase(pre)){
            try {
                writer.write("</pre>");
            } catch (IOException e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Could not print out pre tag", e);
                }
            }
        }
        if (label != null) {
            try {
                writer.write("</div></div>");
            } catch (IOException e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Could not print out value '" + label + "'", e);
                }
            }
        }
        if (evalBody) {
            return component.usesBody() ? EVAL_BODY_BUFFERED : EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }
}
