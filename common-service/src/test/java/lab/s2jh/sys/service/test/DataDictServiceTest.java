package lab.s2jh.sys.service.test;

import java.util.List;
import java.util.Map;

import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.core.test.TestObjectUtils;
import lab.s2jh.sys.entity.DataDict;
import lab.s2jh.sys.service.DataDictService;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DataDictServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private DataDictService dataDictService;

    @Test
    public void findByPage() {
        //Insert mock entity
        DataDict entity = TestObjectUtils.buildMockObject(DataDict.class);
        dataDictService.save(entity);
        Assert.assertTrue(entity.getId() != null);

        //JPA/Hibernate query validation
        List<DataDict> items = dataDictService.findAll(entity.getId());
        Assert.assertTrue(items.size() >= 1);
    }

    @Test
    public void findMapDataByPrimaryKey() {
        //Insert mock entity
        DataDict entity = TestObjectUtils.buildMockObject(DataDict.class);
        entity.setPrimaryKey("ABC");
        entity.setParent(null);
        entity.setDisabled(false);
        dataDictService.save(entity);

        DataDict entity2 = TestObjectUtils.buildMockObject(DataDict.class);
        entity2.setPrimaryKey("A");
        entity2.setSecondaryKey(null);
        entity2.setParent(entity);
        entity2.setDisabled(false);
        dataDictService.save(entity2);

        DataDict entity3 = TestObjectUtils.buildMockObject(DataDict.class);
        entity3.setPrimaryKey("B");
        entity3.setSecondaryKey(null);
        entity3.setParent(entity);
        entity3.setDisabled(false);
        dataDictService.save(entity3);

        Map<String, String> items = dataDictService.findMapDataByPrimaryKey("ABC");
        Assert.assertTrue(items.size() == 2);
        logger.debug("Map Data: {}", items);
    }
}