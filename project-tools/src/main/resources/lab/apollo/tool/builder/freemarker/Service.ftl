package ${root_package}.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import ${root_package}.entity.${entity_name};
import ${root_package}.dao.${entity_name}Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ${entity_name}Service extends BaseService<${entity_name},String>{
    
    @Autowired
    private ${entity_name}Dao ${entity_name_uncapitalize}Dao;

    @Override
    protected BaseDao<${entity_name}, String> getEntityDao() {
        return ${entity_name_uncapitalize}Dao;
    }
}
