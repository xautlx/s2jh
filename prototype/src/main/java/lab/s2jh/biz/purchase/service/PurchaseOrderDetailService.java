package lab.s2jh.biz.purchase.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.s2jh.biz.purchase.dao.PurchaseOrderDetailDao;
import lab.s2jh.biz.purchase.entity.PurchaseOrderDetail;

@Service
@Transactional
public class PurchaseOrderDetailService extends BaseService<PurchaseOrderDetail, Long> {

    @Autowired
    private PurchaseOrderDetailDao purchaseOrderDetailDao;

    @Override
    protected BaseDao<PurchaseOrderDetail, Long> getEntityDao() {
        return purchaseOrderDetailDao;
    }
}
