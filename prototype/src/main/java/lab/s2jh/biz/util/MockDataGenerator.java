package lab.s2jh.biz.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import lab.s2jh.biz.finance.entity.BizTradeUnit;
import lab.s2jh.biz.finance.entity.BizTradeUnit.BizTradeUnitTypeEnum;
import lab.s2jh.biz.md.entity.Commodity;
import lab.s2jh.biz.stock.entity.StorageLocation;
import lab.s2jh.core.util.MockEntityUtils;

import org.apache.commons.lang3.RandomStringUtils;
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
            count = randomNum(5, 10);
            DecimalFormat df = new DecimalFormat("000");
            for (int i = 0; i < count; i++) {
                StorageLocation entity = MockEntityUtils.buildMockObject(StorageLocation.class);
                String num = df.format(i);
                entity.setCode("L" + num);
                entity.setTitle("模拟仓库" + num);
                entity.setAddr("仓库地址" + num);
                entityManager.persist(entity);
            }
            entityManager.flush();
            entityManager.getTransaction().commit();

            entityManager.getTransaction().begin();
            count = randomNum(100, 300);
            List<StorageLocation> storageLocations = entityManager.createQuery("from StorageLocation").getResultList();
            for (int i = 0; i < count; i++) {
                String num = df.format(i);
                Commodity entity = MockEntityUtils.buildMockObject(Commodity.class);
                entity.setDefaultStorageLocation(storageLocations.get(randomNum(0, storageLocations.size() - 1)));
                entity.setSalePrice(new BigDecimal(entity.getSalePrice().intValue()));
                entity.setSku(RandomStringUtils.randomNumeric(8));
                entity.setTitle("模拟商品" + num);
                entity.setBarcode(RandomStringUtils.randomNumeric(13));
                entityManager.persist(entity);
            }
            entityManager.flush();
            entityManager.getTransaction().commit();

            //往来单位
            entityManager.getTransaction().begin();
            count = randomNum(5, 10);
            for (int i = 0; i < count; i++) {
                String num = df.format(i);
                BizTradeUnit entity = MockEntityUtils.buildMockObject(BizTradeUnit.class);
                entity.setName("客户" + num);
                entity.setCode("KH" + num);
                entity.setType(BizTradeUnitTypeEnum.CUSTOMER);
                entityManager.persist(entity);
            }
            count = randomNum(3, 8);
            for (int i = 0; i < count; i++) {
                String num = df.format(i);
                BizTradeUnit entity = MockEntityUtils.buildMockObject(BizTradeUnit.class);
                entity.setName("供应商" + num);
                entity.setCode("GYS" + num);
                entity.setType(BizTradeUnitTypeEnum.SUPPLIER);
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
