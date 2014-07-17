package lab.s2jh.biz.purchase.dao;

import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

import lab.s2jh.biz.purchase.entity.PurchaseOrderDetail;

@Repository
public interface PurchaseOrderDetailDao extends BaseDao<PurchaseOrderDetail, Long> {

}