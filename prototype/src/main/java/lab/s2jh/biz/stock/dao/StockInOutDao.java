package lab.s2jh.biz.stock.dao;

import java.util.List;

import lab.s2jh.biz.core.constant.VoucherTypeEnum;
import lab.s2jh.biz.stock.entity.StockInOut;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface StockInOutDao extends BaseDao<StockInOut, Long> {
    List<StockInOut> findByVoucherAndVoucherType(String voucher, VoucherTypeEnum voucherType);
}