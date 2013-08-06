package lab.s2jh.sys.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.sys.entity.ConfigProperty;
import lab.s2jh.sys.dao.ConfigPropertyDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfigPropertyService extends BaseService<ConfigProperty,String>{
    
    @Autowired
    private ConfigPropertyDao configPropertyDao;

    @Override
    protected BaseDao<ConfigProperty, String> getEntityDao() {
        return configPropertyDao;
    }
}
