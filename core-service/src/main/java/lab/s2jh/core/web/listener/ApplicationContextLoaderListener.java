package lab.s2jh.core.web.listener;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

/**
 * 简单扩展Spring标准的ContextLoaderListener，以便兼容共享jar部署模式
 */
public class ApplicationContextLoaderListener extends ContextLoaderListener {

    private final Logger logger = LoggerFactory.getLogger(ApplicationContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        if (logger.isInfoEnabled()) {
            ClassLoader originalLoader = Thread.currentThread().getContextClassLoader();
            logger.info("Using ClassLoader[{}]: {}", originalLoader.hashCode(), originalLoader);
        }
        super.contextInitialized(event);
    }

}
