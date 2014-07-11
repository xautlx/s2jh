package lab.s2jh.core.cache;

import java.util.Properties;

import net.sf.ehcache.CacheManager;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.hibernate.cfg.Settings;
import org.springframework.beans.factory.FactoryBean;

/**
 * Spring和Hibernate共享CacheManager
 */
public class SpringEhCacheRegionFactory extends EhCacheRegionFactory implements FactoryBean<CacheManager> {

    private static final long serialVersionUID = 3038616259261463760L;

    private static CacheManager cacheManager;

    public SpringEhCacheRegionFactory() {
        super();
    }

    public SpringEhCacheRegionFactory(Properties prop) {
        super(prop);
    }

    public void start(Settings settings, Properties properties) throws CacheException {
        super.start(settings, properties);
        cacheManager = this.manager;
    }

    public CacheManager getObject() {
        return SpringEhCacheRegionFactory.cacheManager;
    }

    public Class<? extends CacheManager> getObjectType() {
        return (SpringEhCacheRegionFactory.cacheManager != null ? SpringEhCacheRegionFactory.cacheManager.getClass()
                : CacheManager.class);
    }

    public boolean isSingleton() {
        return true;
    }

}
