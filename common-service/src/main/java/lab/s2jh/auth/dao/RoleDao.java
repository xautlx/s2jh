package lab.s2jh.auth.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.auth.entity.Role;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends BaseDao<Role, String> {
    @Query("from Role")
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    List<Role> findAllCached();
    
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    Role findByCode(String code);
}