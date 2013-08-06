package ${root_package}.dao.hibernate;

import ${root_package}.dao.${entity_name}Dao;
import ${root_package}.entity.${entity_name};
import lab.apollo.core.dao.hibernate.HibernateDao;

import org.springframework.stereotype.Repository;


@Repository("${entity_name_uncapitalize}Dao")
public class ${entity_name}HibernateDao extends HibernateDao<${entity_name}, Long> implements ${entity_name}Dao {

}
