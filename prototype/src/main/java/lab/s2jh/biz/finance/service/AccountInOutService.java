package lab.s2jh.biz.finance.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lab.s2jh.biz.core.constant.VoucherTypeEnum;
import lab.s2jh.biz.finance.dao.AccountInOutDao;
import lab.s2jh.biz.finance.dao.AccountSubjectDao;
import lab.s2jh.biz.finance.entity.AccountInOut;
import lab.s2jh.biz.finance.entity.AccountSubject;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.service.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class AccountInOutService extends BaseService<AccountInOut, Long> {

    @Autowired
    private AccountInOutDao accountInOutDao;

    @Autowired
    private AccountSubjectDao accountSubjectDao;

    @Override
    protected BaseDao<AccountInOut, Long> getEntityDao() {
        return accountInOutDao;
    }

    public List<AccountInOut> findByVoucherAndVoucherType(String voucher, VoucherTypeEnum voucherType) {
        return accountInOutDao.findByVoucherAndVoucherType(voucher, voucherType);
    }

    @Deprecated
    @Override
    public AccountInOut save(AccountInOut entity) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public List<AccountInOut> save(Iterable<AccountInOut> entities) {
        throw new UnsupportedOperationException();
    }

    public void saveBalance(List<AccountInOut> entities) {
        //试算平衡
        BigDecimal balanceD = BigDecimal.ZERO;
        BigDecimal balanceC = BigDecimal.ZERO;
        for (AccountInOut entity : entities) {
            BigDecimal amount = entity.getAmount();
            if (entity.getAccountDirection()) {
                balanceD = balanceD.add(amount);
            } else {
                balanceC = balanceC.add(amount);
            }
        }
        Validation.isTrue(balanceD.compareTo(balanceC) == 0, "明细账借贷方向试算不平衡，请检查业务逻辑");

        for (AccountInOut entity : entities) {
            if (entity.getDocumentDate() == null) {
                entity.setDocumentDate(new Date());
            }
            if (entity.getAccountSubject() == null) {
                AccountSubject accountSubject = accountSubjectDao.findByCode(entity.getAccountSubjectCode());
                Validation.notNull(accountSubject, entity.getAccountSubjectCode() + ": 未定义的会计科目代码");
                entity.setAccountSubject(accountSubject);
            } else {
                AccountSubject accountSubject = accountSubjectDao.findOne(entity.getAccountSubject().getId());
                entity.setAccountSubject(accountSubject);
                entity.setAccountSubjectCode(accountSubject.getCode());
            }
            Validation.notNull(entity.getAccountSubject().getBalanceDirection(), entity.getAccountSubjectCode()
                    + ": 未配置余额借贷方向");
            if (entity.getAccountDirection().equals(entity.getAccountSubject().getBalanceDirection())) {
                entity.setDirectionAmount(entity.getAmount());
            } else {
                entity.setDirectionAmount(entity.getAmount().negate());
            }
            Assert.isTrue(entity.getAccountSubject() != null || entity.getAccountSubjectCode() != null);
            accountInOutDao.save(entity);
        }
    }

    public void redword(String voucher, VoucherTypeEnum voucherType, String redwordVoucher) {
        List<AccountInOut> accountInOuts = accountInOutDao.findByVoucherAndVoucherType(voucher, voucherType);
        for (AccountInOut accountInOut : accountInOuts) {
            entityManager.detach(accountInOut);
            accountInOut.setId(null);
            accountInOut.setVoucher(redwordVoucher);
            accountInOut.setAmount(accountInOut.getAmount().negate());
            accountInOutDao.save(accountInOut);
        }
    }
}
