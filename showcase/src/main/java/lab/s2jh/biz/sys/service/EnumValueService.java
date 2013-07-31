package lab.s2jh.biz.sys.service;

import java.util.List;
import java.util.Map;

import lab.s2jh.biz.sys.dao.EnumValueDao;
import lab.s2jh.biz.sys.entity.EnumValue;
import lab.s2jh.biz.sys.entity.EnumType.EnumTypes;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

@Service
@Transactional
public class EnumValueService extends BaseService<EnumValue, String> {

    @Autowired
    private EnumValueDao enumValueDao;

    @Override
    protected BaseDao<EnumValue, String> getEntityDao() {
        return enumValueDao;
    }
    
    @Transactional(readOnly = true)
    public List<EnumValue> findByEnumType(String enumType){
        return enumValueDao.findByEnumType(enumType);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "EnumValueSpringCache")
    public Map<String, String> findDisplayItemsByEnumType(String enumType) {
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        List<EnumValue> enumValues = enumValueDao.findByEnumTypeAndDisplay(enumType, true);
        for (EnumValue enumValue : enumValues) {
            dataMap.put(enumValue.getCode(), enumValue.getLabel());
        }
        return dataMap;
    }

    @Async
    @Transactional(readOnly = true)
    public void asyncInitCacheData() {
        EnumTypes[] types = EnumTypes.values();
        for (EnumTypes type : types) {
            findDisplayItemsByEnumType(type.name());
        }
    }
}
