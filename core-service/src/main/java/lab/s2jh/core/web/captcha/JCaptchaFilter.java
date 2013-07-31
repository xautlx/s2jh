package lab.s2jh.core.web.captcha;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lab.s2jh.core.context.KernelConfigParameters;
import lab.s2jh.core.context.SpringContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.WebAttributes;
import org.springframework.util.Assert;

public class JCaptchaFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(JCaptchaFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        KernelConfigParameters kernelConfigParameters = SpringContextHolder.getBean(KernelConfigParameters.class);
        //为了提高开发人员频繁登录效率问题，开发模式则跳过验证码验证
        if (kernelConfigParameters != null && kernelConfigParameters.isDevMode()) {
            logger.warn("Application running at DEV mode, jcaptcha validation skipped.");
            chain.doFilter(request, response);
            return;
        }
        String jCaptcha = request.getParameter("j_captcha");
        Assert.notNull(jCaptcha);
        boolean pass = false;
        try {
            pass = ImageCaptchaServlet.validateResponse(request, jCaptcha);
            if (pass) {
                chain.doFilter(request, response);
                return;
            } else {
                session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new BadCaptchaException(
                        "JCaptcha validate failure"));
            }
        } catch (Exception e) {
            session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new BadCaptchaException(
                    "JCaptcha validate failure", e));
        }
        response.sendRedirect(request.getContextPath() + "/pub/signin");
    }

    @Override
    public void destroy() {

    }

}
