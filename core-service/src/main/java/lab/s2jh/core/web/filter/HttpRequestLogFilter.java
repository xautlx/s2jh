/**
 * Copyright (c) 2012
 */
package lab.s2jh.core.web.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lab.s2jh.core.security.AuthContextHolder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 打印输出HTTP请求信息，一般用于开发调试
 * 生产环境把日志级别设定高于INFO即可屏蔽调试信息输出
 */
public class HttpRequestLogFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(HttpRequestLogFilter.class);

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        logger.debug("Invoking HttpRequestLogFilter init method...");
    }

    @Override
    public void destroy() {
        logger.debug("Invoking HttpRequestLogFilter destroy method...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse reponse, FilterChain chain) throws IOException,
            ServletException {
        if (logger.isInfoEnabled()) {
            HttpServletRequest req = (HttpServletRequest) request;
            String uri = req.getRequestURI();
            if (uri == null || uri.endsWith(".js") || uri.endsWith(".css") || uri.endsWith(".gif")
                    || uri.endsWith(".png") || uri.endsWith(".jpg") || uri.endsWith(".woff") || uri.endsWith(".ico")) {
                chain.doFilter(request, reponse);
                return;
            }

            logger.info(buildPrintMessage(req, logger.isDebugEnabled()));
        }
        chain.doFilter(request, reponse);
    }

    public static String buildPrintMessage(HttpServletRequest req, boolean debugable) {
        String userpin = "NA";
        try {
            userpin = AuthContextHolder.getAuthUserPin();
        } catch (Exception e) {
            //do nothing
        }

        String requestUri = (String) req.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = (String) req.getAttribute("javax.servlet.forward.request_uri");
        }
        if (requestUri == null) {
            requestUri = req.getRequestURI();
        }

        StringBuilder sb = new StringBuilder();
        int padSize = 30;
        String xForwardedFor = req.getHeader("x-forwarded-for");
        sb.append(StringUtils.rightPad("\n HTTP Request Logon User PIN", padSize) + ":" + userpin);
        sb.append(StringUtils.rightPad("\n HTTP Request RemoteAddr", padSize) + ":" + req.getRemoteAddr());
        sb.append(StringUtils.rightPad("\n HTTP Request RemoteHost", padSize) + ":" + req.getRemoteHost());
        sb.append(StringUtils.rightPad("\n HTTP Request x-forwarded-for", padSize) + ":" + xForwardedFor);
        sb.append(StringUtils.rightPad("\n HTTP Request Method", padSize) + ":" + req.getMethod());
        sb.append(StringUtils.rightPad("\n HTTP Request URI", padSize) + ":" + requestUri);
        sb.append(StringUtils.rightPad("\n HTTP Request Query String", padSize) + ":" + req.getQueryString());
        if (debugable) {
            sb.append("\nHTTP Request Parameter List : ");
            Enumeration<String> paramNames = req.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                String paramValue = StringUtils.join(req.getParameterValues(paramName), ",");
                if (paramValue != null && paramValue.length() > 100) {
                    sb.append("\n - " + paramName + "=" + paramValue.substring(0, 100) + "...");
                } else {
                    sb.append("\n - " + paramName + "=" + paramValue);
                }
            }

            sb.append("\nRequest Header Data:");
            java.util.Enumeration headerNames = req.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = (String) headerNames.nextElement();
                sb.append("\n - " + headerName + "=" + req.getHeader(headerName));
            }

            sb.append("\nRequest Attribute Data:");
            java.util.Enumeration attrNames = req.getAttributeNames();
            while (attrNames.hasMoreElements()) {
                String attrName = (String) attrNames.nextElement();
                Object attr = req.getAttribute(attrName);
                if (attr != null && attr.toString().length() > 100) {
                    sb.append("\n - " + attrName + "=" + attr.toString().substring(0, 100) + "...");
                } else {
                    sb.append("\n - " + attrName + "=" + attr);
                }
            }

            sb.append("\nSession Attribute Data:");
            HttpSession session = req.getSession();
            java.util.Enumeration sessionAttrNames = session.getAttributeNames();
            while (sessionAttrNames.hasMoreElements()) {
                String attrName = (String) sessionAttrNames.nextElement();
                Object attr = session.getAttribute(attrName);
                if (attr != null && attr.toString().length() > 100) {
                    sb.append("\n - " + attrName + "=" + attr.toString().substring(0, 100) + "...");
                } else {
                    sb.append("\n - " + attrName + "=" + attr);
                }
            }
        }
        return sb.toString();
    }
}
