package lab.s2jh.auth.dao;

import lab.s2jh.auth.entity.Department;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentDao extends BaseDao<Department, String> {

}
