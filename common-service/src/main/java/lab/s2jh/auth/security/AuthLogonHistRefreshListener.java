package lab.s2jh.auth.security;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import lab.s2jh.auth.entity.UserLogonLog;
import lab.s2jh.auth.service.UserLogonLogService;
import lab.s2jh.core.context.SpringContextHolder;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.util.CollectionUtils;

public class AuthLogonHistRefreshListener implements HttpSessionListener, ServletContextListener {

    private final static Logger logger = LoggerFactory.getLogger(AuthLogonHistRefreshListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //Do nothing
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String sessionId = session.getId();
        UserLogonLogService userLogonLogService = SpringContextHolder.getBean(UserLogonLogService.class);
        UserLogonLog userLogonLog = userLogonLogService.findBySessionId(sessionId);
        if (userLogonLog != null) {
            logger.debug("Setup logout time for session ID: {}", sessionId);
            userLogonLog.setLogoutTime(new Date());
            userLogonLog.setLogonTimeLength(userLogonLog.getLogoutTime().getTime()
                    - userLogonLog.getLogonTime().getTime());
            userLogonLogService.save(userLogonLog);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //Do nothing
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //在容器销毁时把未正常结束遗留的登录记录信息强制设置登出时间
        logger.info("ServletContext destroy force setup session user logout time...");

        UserLogonLogService userLogonLogService = SpringContextHolder.getBean(UserLogonLogService.class);
        GroupPropertyFilter groupPropertyFilter = new GroupPropertyFilter();
        groupPropertyFilter.and(new PropertyFilter(MatchType.NU, "logoutTime", Boolean.TRUE));
        List<UserLogonLog> userLogonLogs = userLogonLogService.findByFilters(groupPropertyFilter);
        if (!CollectionUtils.isEmpty(userLogonLogs)) {

            Set<String> sessionIdSet = new HashSet<String>();
            SessionRegistry sessionRegistry = SpringContextHolder.getBean(SessionRegistry.class);
            List<Object> principals = sessionRegistry.getAllPrincipals();
            for (Object principal : principals) {
                List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(principal, true);
                for (SessionInformation sessionInformation : sessionInformations) {
                    sessionIdSet.add(sessionInformation.getSessionId());
                }
            }
            Date now = new Date();
            Date yesterday = new DateTime().minusDays(1).toDate();
            for (UserLogonLog userLogonLog : userLogonLogs) {
                if (userLogonLog.getLogonTime().before(yesterday)) {
                    Date logoutTime = new DateTime(userLogonLog.getLogonTime()).plusHours(1).toDate();
                    userLogonLog.setLogoutTime(logoutTime);
                } else {
                    if (sessionIdSet.contains(userLogonLog.getHttpSessionId())) {
                        userLogonLog.setLogoutTime(now);
                    } else {
                        continue;
                    }
                }
                logger.debug(" - Setup logout time for session ID: {}", userLogonLog.getHttpSessionId());
                userLogonLog.setLogonTimeLength(userLogonLog.getLogoutTime().getTime()
                        - userLogonLog.getLogonTime().getTime());
                userLogonLogService.save(userLogonLog);
            }
        }
    }
}
