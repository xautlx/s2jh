package lab.s2jh.biz.finance.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.biz.finance.entity.AccountSubject;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountSubjectDao extends BaseDao<AccountSubject, Long> {

    @Query("from AccountSubject")
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<AccountSubject> findAllCached();

    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    public AccountSubject findByCode(String code);
}