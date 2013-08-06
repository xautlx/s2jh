package ${root_package}.dao;

import ${root_package}.entity.${entity_name};
import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface ${entity_name}Dao extends BaseDao<${entity_name}, String> {

}