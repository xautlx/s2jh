package lab.s2jh.biz.finance.dao;

import java.util.List;

import lab.s2jh.biz.core.constant.VoucherTypeEnum;
import lab.s2jh.biz.finance.entity.AccountInOut;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface AccountInOutDao extends BaseDao<AccountInOut, Long> {

    List<AccountInOut> findByVoucherAndVoucherType(String voucher, VoucherTypeEnum voucherType);

}