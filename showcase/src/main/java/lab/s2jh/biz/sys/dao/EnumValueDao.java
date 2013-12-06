package lab.s2jh.biz.sys.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.biz.sys.entity.EnumValue;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;


@Repository
public interface EnumValueDao extends BaseDao<EnumValue, String> {

    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    List<EnumValue> findByEnumTypeAndShow(String enumType, Boolean display);
    
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    List<EnumValue> findByEnumType(String enumType);
}