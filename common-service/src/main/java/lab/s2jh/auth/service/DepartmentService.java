package lab.s2jh.auth.service;

import lab.s2jh.auth.dao.DepartmentDao;
import lab.s2jh.auth.entity.Department;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepartmentService extends BaseService<Department, String> {

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    protected BaseDao<Department, String> getEntityDao() {
        return departmentDao;
    }

}
