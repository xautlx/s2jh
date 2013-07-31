package lab.s2jh.core.web.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;

public class ServletUtils {

    public static Map<String, String[]> getParametersStartingWith(ServletRequest request, String prefix, String suffix) {
        Assert.notNull(request, "Request must not be null");
        @SuppressWarnings("rawtypes")
        Enumeration paramNames = request.getParameterNames();
        Map<String, String[]> params = new TreeMap<String, String[]>();
        if (prefix == null) {
            prefix = "";
        }
        if (suffix == null) {
            suffix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if (("".equals(prefix) || paramName.startsWith(prefix))
                    && ("".equals(suffix) || paramName.endsWith(suffix))) {
                String unprefixed = paramName.substring(prefix.length(), paramName.length() - suffix.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, new String[] { values[0] });
                }
            }
        }
        return params;
    }
    

    /**
     * 设置让浏览器弹出下载对话框的Header.
     * 
     * @param fileName 下载后的文件名.
     */
    public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
        try {
            //中文文件名支持
            String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
        } catch (UnsupportedEncodingException e) {
        }
    }

    public static void renderFileDownload(HttpServletResponse response, byte[] fileData) {
        byte[] buffer = new byte[4096];
        BufferedOutputStream output = null;
        ByteArrayInputStream input = null;
        try {
            output = new BufferedOutputStream(response.getOutputStream());
            input = new ByteArrayInputStream(fileData);

            int n = (-1);
            while ((n = input.read(buffer, 0, 4096)) > -1) {
                output.write(buffer, 0, n);
            }
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (input != null)
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (output != null)
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
