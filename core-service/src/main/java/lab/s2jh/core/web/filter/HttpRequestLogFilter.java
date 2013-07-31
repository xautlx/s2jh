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

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest request, ServletResponse reponse, FilterChain chain) throws IOException,
            ServletException {
        if (logger.isDebugEnabled()) {
            HttpServletRequest req = (HttpServletRequest) request;
            String uri = req.getRequestURI();
            if (uri == null || uri.endsWith(".js") || uri.endsWith(".css") || uri.endsWith(".gif")
                    || uri.endsWith(".png") || uri.endsWith(".jpg")) {
                chain.doFilter(request, reponse);
                return;
            }
            String userpin = "NA";
            try {
                userpin = AuthContextHolder.getAuthUserPin();
            } catch (Exception e) {
                //do nothing
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n HTTP Request Logon User PIN : " + userpin);
            sb.append("\n HTTP Request Logon User IP  : " + req.getRemoteAddr());
            sb.append("\n HTTP Request Logon User Host: " + req.getRemoteHost());
            sb.append("\n HTTP Request Method         : " + req.getMethod());
            sb.append("\n HTTP Request URL            : " + req.getRequestURL());
            sb.append("\n HTTP Request Query String   : " + req.getQueryString());
            sb.append("\n HTTP Request Parameter List : ");
            Enumeration<String> paramNames = req.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                String paramValue = StringUtils.join(request.getParameterValues(paramName), ",");
                if (paramValue != null && paramValue.length() > 100) {
                    sb.append("\n - " + paramName + "=" + paramValue.substring(0, 50) + "...");
                } else {
                    sb.append("\n - " + paramName + "=" + paramValue + "");
                }
            }

            Boolean debug = BooleanUtils.toBoolean(request.getParameter("debug"));
            if (debug) {
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
                        sb.append("\n - " + attrName + "=" + attr + "...");
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
                        sb.append("\n - " + attrName + "=" + attr + "...");
                    }
                }
            }

            logger.debug(sb.toString());
        }
        chain.doFilter(request, reponse);
    }

}
