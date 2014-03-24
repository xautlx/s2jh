package lab.s2jh.sys.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.sys.entity.DataDict;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface DataDictDao extends BaseDao<DataDict, String> {

    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    public DataDict findByPrimaryKey(String primaryKey);
    
    @Query("from DataDict where parent=? and disabled=? order by orderRank desc")
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<DataDict> findChildrenByParentAndDisabled(DataDict parent, Boolean disabled);

    @Query("from DataDict order by parent asc,orderRank desc")
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<DataDict> findAllCached();
}