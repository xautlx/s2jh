package lab.s2jh.biz.purchase.dao;

import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

import lab.s2jh.biz.purchase.entity.PurchaseOrder;

@Repository
public interface PurchaseOrderDao extends BaseDao<PurchaseOrder, Long> {


}