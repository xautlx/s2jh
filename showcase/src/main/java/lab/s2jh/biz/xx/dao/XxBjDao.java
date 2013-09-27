package lab.s2jh.biz.xx.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.biz.xx.entity.XxBj;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface XxBjDao extends BaseDao<XxBj, String> {

    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    List<XxBj> findByXxdmAndNj(String xxdm, String nj);

    @Query("select distinct nj,nj from XxBj where xxdm=:xxdm order by nj desc")
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    List<Object[]> findNjsOfXxdm(@Param("xxdm") String xxdm);

    XxBj findByBh(String bh);

    XxBj findByXxdmAndBh(String xxdm, String bh);

    @Query("select t from XxBj t where t.xxdm=:xxdm and (t.bh like :term or t.bjmc like :term) order by nj desc")
    List<XxBj> findByXxdmAndBhOrBjmcLike(@Param("xxdm") String xxdm, @Param("term") String bhTerm,
            @Param("term") String bjmcTerm);

    @Query("select t from XxBj t where (t.bh like :term or t.bjmc like :term) order by nj desc")
    List<XxBj> findByBhOrBjmcLike(@Param("term") String bhTerm, @Param("term") String bjmcTerm);
    
    @Query("select count(*) from XxBj t where t.bh = :bh")
    Long findXsCount(@Param("bh") String bh);
}