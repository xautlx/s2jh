package lab.s2jh.sys.dao;

import javax.persistence.QueryHint;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.sys.entity.Menu;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuDao extends BaseDao<Menu, String> {

    @Query("from Menu")
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    public Iterable<Menu> findAllCached();
}
