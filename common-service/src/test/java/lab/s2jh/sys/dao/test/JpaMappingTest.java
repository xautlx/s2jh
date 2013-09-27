package lab.s2jh.sys.dao.test;

import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import lab.s2jh.core.test.SpringTransactionalTestCase;

import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

public class JpaMappingTest extends SpringTransactionalTestCase {

    private static Logger logger = LoggerFactory.getLogger(JpaMappingTest.class);

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("rawtypes")
    @Test
    public void allClassMapping() throws Exception {
        Metamodel model = em.getEntityManagerFactory().getMetamodel();

        assertTrue("No entity mapping found", model.getEntities().size() > 0);

        for (EntityType entityType : model.getEntities()) {
            String entityName = entityType.getName();
            if (!"DefaultRevisionEntity".equals(entityName)) {
                em.createQuery("select o from " + entityName + " o").getResultList();
                logger.info("ok: " + entityName);
            }
        }

        EntityManagerFactoryInfo entityManagerFactoryInfo = (EntityManagerFactoryInfo) applicationContext
                .getBean("entityManagerFactory");
        EntityManagerFactory emf = entityManagerFactoryInfo.getNativeEntityManagerFactory();
        EntityManagerFactoryImpl emfImp = (EntityManagerFactoryImpl) emf;
        logger.debug("Hibernate Cache Statistics: {}", emfImp.getSessionFactory().getStatistics());
    }
}
