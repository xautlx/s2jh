package lab.s2jh.auth.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.auth.entity.Privilege;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeDao extends BaseDao<Privilege, String> {

    @Query("from Privilege order by category asc")
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    List<Privilege> findAllCached();

    @Query("select distinct category from Privilege")
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    List<String> findDistinctCategories();

    Iterable<Privilege> findByDisabled(Boolean disabled);

    Privilege findByCode(String code);

    @Query("select distinct p from Privilege p,RoleR2Privilege r2p,UserR2Role u2r,Role r "
            + "where p=r2p.privilege and r2p.role=u2r.role and r2p.role=r "
            + "and u2r.user.id=:userId and r.disabled=false and p.disabled=false order by p.category asc")
    List<Privilege> findPrivilegesForUser(@Param("userId") String userId);
}
