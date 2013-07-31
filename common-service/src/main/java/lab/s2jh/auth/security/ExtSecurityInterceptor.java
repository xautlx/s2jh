package lab.s2jh.auth.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * 植入指定的securityMetadataSource
 */
public class ExtSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    private static Logger logger = LoggerFactory.getLogger(ExtSecurityInterceptor.class);

    private FilterInvocationSecurityMetadataSource securityMetadataSource;
    
    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("ExtSecurityInterceptor init...");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        logger.debug("ExtSecurityInterceptor doFilter...");
        FilterInvocation fi = new FilterInvocation(req, response, chain);
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {
        logger.debug("ExtSecurityInterceptor destroy...");
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

}
