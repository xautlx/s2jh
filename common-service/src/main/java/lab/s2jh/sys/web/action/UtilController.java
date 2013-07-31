package lab.s2jh.sys.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.web.annotation.SecurityControllIgnore;
import lab.s2jh.core.web.view.OperationResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.RestActionSupport;
import org.h2.tools.Server;
import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

import com.opensymphony.xwork2.ModelDriven;

@MetaData(title = "辅助功能")
public class UtilController extends RestActionSupport implements ModelDriven<Object> {

    protected final static Logger logger = LoggerFactory.getLogger(UtilController.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private CacheManager cacheManager;

    @Autowired(required = false)
    private Server h2Server;

    private Object model;

    @Override
    public Object getModel() {
        return model;
    }

    @SecurityControllIgnore
    public HttpHeaders index() {
        return new DefaultHttpHeaders("index");
    }

    @SecurityControllIgnore
    public HttpHeaders logger() {
        return new DefaultHttpHeaders("logger");
    }

    public List<String> getCacheNames() {
        List<String> datas = new ArrayList<String>();
        for (String cacheName : cacheManager.getCacheNames()) {
            datas.add(cacheName);
        }
        Collections.sort(datas);
        return datas;
    }

    @MetaData(title = "刷新数据缓存")
    public HttpHeaders dataEvictCache() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String[] cacheNames = request.getParameterValues("cacheNames");

        SessionFactory sessionFactory = ((HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory();

        if (cacheNames != null && cacheNames.length > 0 && cacheNames[0].trim().length() > 0) {
            if (logger.isInfoEnabled()) {
                logger.info("Evicting Hibernate&Spring Cache, Scope: {}", StringUtils.join(cacheNames, ","));
            }
            for (String cacheName : cacheNames) {
                if (StringUtils.isNotBlank(cacheName)) {
                    sessionFactory.getCache().evictQueryRegion(cacheName);
                    sessionFactory.getCache().evictEntityRegion(cacheName);
                    cacheManager.getCache(cacheName).clear();
                }
            }
        } else {
            logger.info("Evicting Hibernate&Spring Cache, Scope: All");
            sessionFactory.getCache().evictEntityRegions();
            sessionFactory.getCache().evictCollectionRegions();
            sessionFactory.getCache().evictQueryRegions();
            for (String cacheName : cacheManager.getCacheNames()) {
                cacheManager.getCache(cacheName).clear();
            }
        }
        model = OperationResult.buildSuccessResult("数据缓存刷新操作成功");
        return new DefaultHttpHeaders().disableCaching();
    }

    public Map<String, String> getLoggerList() {
        Map<String, String> dataMap = new LinkedHashMap<String, String>();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<ch.qos.logback.classic.Logger> loggers = loggerContext.getLoggerList();
        for (Logger logger : loggers) {
            dataMap.put(logger.getName(), logger.getName());
        }
        return dataMap;
    }

    @MetaData(title = "日志级别更新")
    public HttpHeaders loggerLevelUpdate() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String loggerName = request.getParameter("loggerName");
        String loggerLevel = request.getParameter("loggerLevel");
        if (StringUtils.isBlank(loggerName)) {
            loggerName = Logger.ROOT_LOGGER_NAME;
        }
        Logger logger = LoggerFactory.getLogger(loggerName);
        ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger) logger;
        if (StringUtils.isNotBlank(loggerLevel)) {
            logbackLogger.setLevel(Level.toLevel(loggerLevel));
        }
        model = OperationResult.buildSuccessResult("日志参数刷新操作成功");
        return new DefaultHttpHeaders().disableCaching();
    }

    @MetaData(title = "H2数据管理")
    public void h2() {
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            if (h2Server != null) {
                response.sendRedirect("http://localhost:" + h2Server.getPort() + "/login.jsp");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "H2 Server not start");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
