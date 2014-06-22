package lab.s2jh.biz.sale.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.biz.sale.entity.SaleDeliveryDetail;
import lab.s2jh.biz.sale.dao.SaleDeliveryDetailDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SaleDeliveryDetailService extends BaseService<SaleDeliveryDetail,Long>{
    
    @Autowired
    private SaleDeliveryDetailDao saleDeliveryDetailDao;

    @Override
    protected BaseDao<SaleDeliveryDetail, Long> getEntityDao() {
        return saleDeliveryDetailDao;
    }
}
