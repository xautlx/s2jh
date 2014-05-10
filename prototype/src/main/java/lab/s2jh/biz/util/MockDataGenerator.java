package lab.s2jh.biz.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import lab.s2jh.biz.md.entity.Commodity;
import lab.s2jh.biz.stock.entity.StorageLocation;
import lab.s2jh.core.util.MockEntityUtils;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * 模拟数据生成器
 */
public class MockDataGenerator {

    private boolean enabled = false;

    private EntityManagerFactory entityManagerFactory;

    private PlatformTransactionManager transactionManager;

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void initializeDatabase() {
        if (!enabled) {
            return;
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            int count;
            entityManager.getTransaction().begin();
            count = randomNum(10, 30);
            for (int i = 0; i < count; i++) {
                StorageLocation entity = MockEntityUtils.buildMockObject(StorageLocation.class);
                entityManager.persist(entity);
            }
            entityManager.flush();
            entityManager.getTransaction().commit();

            entityManager.getTransaction().begin();
            count = randomNum(100, 300);
            List<StorageLocation> storageLocations = entityManager.createQuery("from StorageLocation").getResultList();
            for (int i = 0; i < count; i++) {
                Commodity entity = MockEntityUtils.buildMockObject(Commodity.class);
                entity.setDefaultStorageLocation(storageLocations.get(randomNum(0, storageLocations.size() - 1)));
                entityManager.persist(entity);
            }
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    private int randomNum(int start, int end) {
        return Double.valueOf(start + Math.random() * (end - start)).intValue();
    }
}
