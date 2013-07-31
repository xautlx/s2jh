package lab.s2jh.biz.xs.dao;

import java.util.Collection;
import java.util.List;

import lab.s2jh.biz.xs.entity.XsJbxx;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;


@Repository
public interface XsJbxxDao extends BaseDao<XsJbxx, String> {
    
    List<XsJbxx> findByXxdmAndXhStartingWith(String xxdm,String xhPrefix);
    
    XsJbxx findByXxdmAndXh(String xxdm,String xh);
    
    List<XsJbxx> findByIdIn(Collection<String> ids);
}