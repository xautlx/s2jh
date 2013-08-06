package lab.s2jh.sys.dao;

import lab.s2jh.sys.entity.ConfigProperty;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface ConfigPropertyDao extends BaseDao<ConfigProperty, String> {

}