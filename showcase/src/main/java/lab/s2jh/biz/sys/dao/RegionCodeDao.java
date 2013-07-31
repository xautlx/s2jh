package lab.s2jh.biz.sys.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.biz.sys.entity.RegionCode;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;


@Repository
public interface RegionCodeDao extends BaseDao<RegionCode, String> {

    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    RegionCode findByRegionCode(String regionCode);

    @Query("select rc.regionCode, rc.regionDesc from RegionCode rc order by rc.regionCode asc")
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    List<Object[]> findKeyValueInfo();
    
}