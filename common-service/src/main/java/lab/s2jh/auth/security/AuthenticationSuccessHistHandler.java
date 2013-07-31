package lab.s2jh.auth.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lab.s2jh.auth.entity.UserLogonLog;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.util.IPAddrFetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

/**
 * Spring Security认证成功处理历史记录
 */
public class AuthenticationSuccessHistHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHistHandler.class);

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        try {

            AuthUserDetails authUserDetails = AuthContextHolder.getAuthUserDetails();

            logger.debug("Removing " + AuthContextHolder.SPRING_SECURITY_LAST_USERNAME_KEY + " from session...");
            HttpSession session = request.getSession(false);
            if (session != null) {
                request.getSession().removeAttribute(AuthContextHolder.SPRING_SECURITY_LAST_USERNAME_KEY);
            }

            logger.debug("Inserting logon history record for: {}", authUserDetails.getUsername());
            //写入登入记录信息
            UserLogonLog userLogonLog = new UserLogonLog();
            userLogonLog.setLogonTime(new Date());
            userLogonLog.setRemoteAddr(request.getRemoteAddr());
            userLogonLog.setRemoteHost(request.getRemoteHost());
            userLogonLog.setRemotePort(request.getRemotePort());
            userLogonLog.setLocalAddr(request.getLocalAddr());
            userLogonLog.setLocalName(request.getLocalName());
            userLogonLog.setLocalPort(request.getLocalPort());
            userLogonLog.setServerIP(IPAddrFetcher.getGuessUniqueIP());
            userLogonLog.setHttpSessionId(request.getSession().getId());
            userLogonLog.setUserAgent(request.getHeader("User-Agent"));
            userLogonLog.setxForwardFor(request.getHeader("X-Forward-For"));
            userLogonLog.setUserid(authUserDetails.getUid());
            userLogonLog.setUsername(authUserDetails.getUsername());
            userService.userLogonLog(userLogonLog);

        } catch (Exception e) {
            logger.error("error.spring.security.insert.logon.hist", e);
        }

        //Hack RememberMe时不能正常返回认证之前的请求
        //参考：https://jira.springsource.org/browse/SEC-1991
        String url = request.getServletPath();
        if (url != null && url.indexOf("j_spring_security") == -1) {
            HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
            requestCache.saveRequest(request, response);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
