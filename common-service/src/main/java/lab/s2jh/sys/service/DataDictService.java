package lab.s2jh.sys.service;

import java.util.List;
import java.util.Map;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.sys.dao.DataDictDao;
import lab.s2jh.sys.entity.DataDict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

@Service
@Transactional
public class DataDictService extends BaseService<DataDict, Long> {

    @Autowired
    private DataDictDao dataDictDao;

    @Autowired
    private MessageSource messageSource;

    @Override
    protected BaseDao<DataDict, Long> getEntityDao() {
        return dataDictDao;
    }

    public List<DataDict> findAllCached() {
        return dataDictDao.findAllCached();
    }

    public List<DataDict> findChildrenByPrimaryKey(String primaryKey) {
        DataDict parent = dataDictDao.findByPrimaryKey(primaryKey);
        return dataDictDao.findChildrenByParentAndDisabled(parent, false);
    }

    public Map<String, String> findMapDataByPrimaryKey(String primaryKey) {
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        List<DataDict> dataDicts = findChildrenByPrimaryKey(primaryKey);
        for (DataDict dataDict : dataDicts) {
            dataMap.put(dataDict.getPrimaryKey(), dataDict.getPrimaryValue());
        }
        return dataMap;
    }
}
