package org.apache.struts2.components;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.views.annotations.StrutsTag;

import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

@StrutsTag(name = "file", tldBodyContent = "empty", tldTagClass = "org.apache.struts2.views.jsp.S2FileTag", description = "Render a file link.")
public class File extends ContextBean {

    private static final Logger LOG = LoggerFactory.getLogger(File.class);

    private String value;

    public File(ValueStack stack) {
        super(stack);
    }

    public boolean end(Writer writer, String body) {
        FileDef fileDef = null;
        // find the value on the valueStack
        try {
            //suport Calendar also
            Object fileObject = findValue(value);
            if (fileObject instanceof FileDef) {
                fileDef = (FileDef) fileObject;
            }
        } catch (Exception e) {
            LOG.error("Could not convert object with key '#0' to a FileDef instance", value);
        }

        if (fileDef != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<a href=\"javascript:void(0)\" onclick=\"fileDownload('" + fileDef.getId() + "')\">");
            sb.append("<i class=\"icon-download\"></i> " + fileDef.getFileRealName() + " ("
                    + FileUtils.byteCountToDisplaySize(fileDef.getFileLength()) + ")");
            sb.append("</a>");
            try {
                writer.write(sb.toString());
            } catch (IOException e) {
                LOG.error("Could not write out Date tag", e);
            }
        }
        return super.end(writer, "");
    }

    public interface FileDef {
        String getFileRealName();

        int getFileLength();

        String getId();
    }

    public void setValue(String value) {
        this.value = value;
    }
}
