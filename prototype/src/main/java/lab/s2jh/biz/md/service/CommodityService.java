package lab.s2jh.biz.md.service;

import lab.s2jh.biz.md.dao.CommodityDao;
import lab.s2jh.biz.md.entity.Commodity;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommodityService extends BaseService<Commodity,String>{
    
    @Autowired
    private CommodityDao commodityDao;

    @Override
    protected BaseDao<Commodity, String> getEntityDao() {
        return commodityDao;
    }
}
