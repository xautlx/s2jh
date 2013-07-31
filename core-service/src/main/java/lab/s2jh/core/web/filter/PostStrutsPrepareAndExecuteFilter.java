package lab.s2jh.core.web.filter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterConfig;

import lab.s2jh.core.web.annotation.SecurityControllIgnore;

import org.apache.commons.lang3.ClassUtils;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.apache.struts2.rest.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.entities.ActionConfig;

public class PostStrutsPrepareAndExecuteFilter extends StrutsPrepareAndExecuteFilter {

    private static final Logger logger = LoggerFactory.getLogger(PostStrutsPrepareAndExecuteFilter.class);

    public static Set<String> securityControllIgnoreUrls = Sets.newHashSet();

    /**
     * Callback for post initialization
     */
    protected void postInit(Dispatcher dispatcher, FilterConfig filterConfig) {
        logger.debug("{} postInit...", this.getClass());
        ConfigurationManager configurationManager = dispatcher.getConfigurationManager();        
        Configuration configuration = configurationManager.getConfiguration();

        Map<String, Map<String, ActionConfig>> allActionConfigs = configuration.getRuntimeConfiguration()
                .getActionConfigs();
        if (allActionConfigs != null) {
            Set<String> namespaces = allActionConfigs.keySet();
            for (String namespace : namespaces) {
                Map<String, ActionConfig> actionConfigMap = allActionConfigs.get(namespace);
                for (Map.Entry<String, ActionConfig> me : actionConfigMap.entrySet()) {
                    String actionName = me.getKey();
                    ActionConfig actionConfig = me.getValue();
                    String className = actionConfig.getClassName();
                    if (!className.startsWith("org.apache.struts")) {
                        logger.trace("Parsing actionConfig={}", actionConfig);
                        try {
                            Class<?> actionClass = ClassUtils.getClass(className);
                            for (Method method : actionClass.getDeclaredMethods()) {
                                SecurityControllIgnore securityControllIgnore = method
                                        .getAnnotation(SecurityControllIgnore.class);
                                if (securityControllIgnore != null) {
                                    if (Modifier.isPublic(method.getModifiers())
                                            && (method.getReturnType() == String.class || method.getReturnType() == HttpHeaders.class)) {
                                        String url = namespace + "/" + actionName + "!" + method.getName();
                                        securityControllIgnoreUrls.add(url);
                                    }
                                }

                            }
                        } catch (ClassNotFoundException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
    }
}
