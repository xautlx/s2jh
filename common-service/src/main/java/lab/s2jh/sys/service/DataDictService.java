package lab.s2jh.sys.service;

import java.util.List;
import java.util.Map;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.security.AuthContextHolder;
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
public class DataDictService extends BaseService<DataDict, String> {

    private final static String MESSAGE_RESOURCE_PREFIX = "data.dict.category.";

    @Autowired
    private DataDictDao dataDictDao;

    @Autowired
    private MessageSource messageSource;

    @Override
    protected BaseDao<DataDict, String> getEntityDao() {
        return dataDictDao;
    }

    public String findCategoryLabel(String category) {
        return messageSource.getMessage(MESSAGE_RESOURCE_PREFIX + category, null, category,
                AuthContextHolder.getLocale());
    }

    /**
     * 返回去重Key-Label形式的分类Map数据
     * @return
     */
    public Map<String, String> findDistinctCategories() {
        Map<String, String> distinctCategories = Maps.newLinkedHashMap();
        List<String> categories = dataDictDao.findDistinctCategories();
        for (String category : categories) {
            distinctCategories.put(category, findCategoryLabel(category));
        }
        return distinctCategories;
    }

    public List<DataDict> findByCategory(String category) {
        return dataDictDao.findByCategoryOrderByOrderRankDesc(category);
    }

    public Map<String, String> findMapDataByCategory(String category) {
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        List<DataDict> dataDicts = findByCategory(category);
        for (DataDict dataDict : dataDicts) {
            dataMap.put(dataDict.getKey1Value(), dataDict.getData1Value());
        }
        return dataMap;
    }
}
