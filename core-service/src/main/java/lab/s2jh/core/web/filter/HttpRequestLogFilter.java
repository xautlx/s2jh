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

    private static final Integer PAD_SIZE = 30;

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

            StringBuilder sb = new StringBuilder("Debug information: ");
            sb.append(StringUtils.rightPad("\n HTTP Request Logon User PIN", PAD_SIZE) + ":"
                    + AuthContextHolder.getAuthUserPin());
            sb.append(StringUtils.rightPad("\n HTTP Request Method", PAD_SIZE) + ":" + req.getMethod());
            sb.append(StringUtils.rightPad("\n HTTP Request URI", PAD_SIZE) + ":" + req.getRequestURI());
            sb.append(StringUtils.rightPad("\n HTTP Request Query String", PAD_SIZE) + ":" + req.getQueryString());
            if (logger.isDebugEnabled()) {
                sb.append("\nHTTP Request Parameter List : ");
                Enumeration<?> paramNames = request.getParameterNames();
                while (paramNames.hasMoreElements()) {
                    String paramName = (String) paramNames.nextElement();
                    String paramValue = StringUtils.join(request.getParameterValues(paramName), ",");
                    if (paramValue != null && paramValue.length() > 100) {
                        sb.append("\n - " + paramName + "=" + paramValue.substring(0, 100) + "...");
                    } else {
                        sb.append("\n - " + paramName + "=" + paramValue);
                    }
                }

                logger.info(sb.toString());
            }
        }
        chain.doFilter(request, reponse);
    }
}
