package lab.s2jh.auth.service;

import java.util.Collections;
import java.util.List;

import lab.s2jh.auth.dao.DepartmentDao;
import lab.s2jh.auth.entity.Department;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

@Service
@Transactional
public class DepartmentService extends BaseService<Department, String> {

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    protected BaseDao<Department, String> getEntityDao() {
        return departmentDao;
    }

    @Transactional(readOnly = true)
    public List<Department> findRoots() {
        List<Department> roots = Lists.newArrayList();
        Iterable<Department> items = departmentDao.findAllCached();
        for (Department item : items) {
            if (item.getParent() == null) {
                roots.add(item);
            }
        }
        Collections.sort(roots);
        return roots;
    }

    @Transactional(readOnly = true)
    public List<Department> findChildren(Department parent) {
        if (parent == null) {
            return findRoots();
        }
        List<Department> children = Lists.newArrayList();
        Iterable<Department> items = departmentDao.findAllCached();
        for (Department item : items) {
            if (parent.equals(item.getParent())) {
                children.add(item);
            }
        }
        Collections.sort(children);
        return children;
    }

    @Transactional(readOnly = true)
    public List<Department> findChildrenCascade(Department parent) {
        Assert.notNull(parent);
        List<Department> children = Lists.newArrayList();
        Iterable<Department> items = departmentDao.findAllCached();
        for (Department item : items) {
            Department loop = item.getParent();
            while (loop != null) {
                if (parent.equals(loop)) {
                    children.add(item);
                    break;
                }
                loop = loop.getParent();
            }
        }
        Collections.sort(children);
        return children;
    }
}
