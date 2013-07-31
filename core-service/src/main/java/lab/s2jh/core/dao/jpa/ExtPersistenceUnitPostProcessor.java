package lab.s2jh.core.dao.jpa;

import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

/**
 * 扩展JPA Hibernate持久对象Post处理逻辑
 * 主要是为了获取MutablePersistenceUnitInfo对象从而可以获取Hibernate Entity元数据
 * 
 */
public class ExtPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {

    private MutablePersistenceUnitInfo mutablePersistenceUnitInfo;

    @Override
    public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
        this.mutablePersistenceUnitInfo = pui;
    }

    public MutablePersistenceUnitInfo getMutablePersistenceUnitInfo() {
        return mutablePersistenceUnitInfo;
    }
}
