package lab.s2jh.core.web.listener;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.context.SpringContextHolder;
import lab.s2jh.core.dao.jpa.ExtPersistenceUnitPostProcessor;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.common.collect.Maps;

/**
 * Spring容器加载“之后”的ServletContextListener
 */
public class ApplicationContextPostListener implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(ApplicationContextPostListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.debug("Invoke ApplicationContextPostListener contextInitialized");
        try {
            ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event
                    .getServletContext());

            SpringContextHolder.setApplicationContext(applicationContext);

            ServletContext sc = event.getServletContext();
            String appName = sc.getServletContextName();
            logger.info("[{}] init context ...", appName);

            Map<String, Map<? extends Serializable, String>> scEnumsMap = Maps.newHashMap();
            //存储枚举名称和对应Class映射的Map
            Map<String, Class<?>> enumShortNameClassMapping = Maps.newHashMap();

            ExtPersistenceUnitPostProcessor persistenceUnitPostProcessor = (ExtPersistenceUnitPostProcessor) applicationContext
                    .getBean(PersistenceUnitPostProcessor.class);

            MutablePersistenceUnitInfo pui = persistenceUnitPostProcessor.getMutablePersistenceUnitInfo();

            //循环所有Entity对象中的Enum定义，转换成对应的Map结构数据存入ServletContext属性中，便于Web层标签直接方便获取
            for (Class<?> entityClass : ClassUtils.convertClassNamesToClasses(pui.getManagedClassNames())) {
                //logger.debug("Post process {}", entityClass);
                Field[] fields = entityClass.getDeclaredFields();
                for (Field field : fields) {
                    Class fieldClass = field.getType();
                    if (fieldClass.isEnum()) {
                        String simpleName = fieldClass.getSimpleName();
                        Class<?> existClass = enumShortNameClassMapping.get(simpleName);
                        if (existClass == null) {
                            //logger.info(" - Put Enum short name mapping: {}={}", simpleName, fieldClass);
                            enumShortNameClassMapping.put(simpleName, fieldClass);
                        } else {
                            if (!existClass.equals(fieldClass)) {
                                throw new IllegalStateException("Duplicate simple name: " + simpleName + ", class1="
                                        + existClass + ", class2=" + fieldClass);
                            } else {
                                continue;
                            }
                        }

                        Map<java.lang.Enum, String> enumDataMap = Maps.newLinkedHashMap();
                        for (Field enumfield : fieldClass.getFields()) {
                            MetaData entityComment = enumfield.getAnnotation(MetaData.class);
                            String value = enumfield.getName();
                            if (entityComment != null) {
                                value = entityComment.title();
                            }
                            enumDataMap.put(Enum.valueOf(fieldClass, enumfield.getName()), value);
                        }
                        String attrName = StringUtils.uncapitalize(fieldClass.getSimpleName());
                        scEnumsMap.put(attrName, enumDataMap);
                    }
                }
            }

            //设置默认的boolean类型数据转义显示
            Map<Boolean, String> booleanLabelMap = Maps.newLinkedHashMap();
            booleanLabelMap.put(Boolean.TRUE, "是");
            booleanLabelMap.put(Boolean.FALSE, "否");
            scEnumsMap.put("booleanLabel", booleanLabelMap);

            if (logger.isInfoEnabled()) {
                logger.info("Put enums data to ServletContext: ");
                for (Map.Entry<String, Map<? extends Serializable, String>> me : scEnumsMap.entrySet()) {
                    logger.info(" -  {} = {}", me.getKey(), me.getValue());
                }
            }
            sc.setAttribute("enums", scEnumsMap);
        } catch (Exception e) {
            logger.error("error detail:", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        logger.debug("Invoke ApplicationContextPostListener contextDestroyed");
    }

}
