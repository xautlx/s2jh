package lab.s2jh.biz.xx.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.biz.xx.entity.XxJcxx;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;


@Repository
public interface XxJcxxDao extends BaseDao<XxJcxx, String> {
    @Query("select xxdm, xxmc from XxJcxx order by xxdm desc")
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    List<Object[]> findKeyValueInfo();
    
    List<XxJcxx> findByXzqhmStartingWithAndSszgdwm(String xzqhmPrefix,String sszgdwmPrefix);
    
    List<XxJcxx> findByXzqhmStartingWith(String xzqhmPrefix);
    
    List<XxJcxx> findBySszgdwm(String sszgdwm);

    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    XxJcxx findByXxdm(String xxdm);
}