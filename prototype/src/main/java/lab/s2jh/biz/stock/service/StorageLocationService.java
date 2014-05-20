package lab.s2jh.biz.stock.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.biz.stock.entity.StorageLocation;
import lab.s2jh.biz.stock.dao.StorageLocationDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StorageLocationService extends BaseService<StorageLocation,String>{
    
    @Autowired
    private StorageLocationDao storageLocationDao;

    @Override
    protected BaseDao<StorageLocation, String> getEntityDao() {
        return storageLocationDao;
    }
}
