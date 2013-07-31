package lab.s2jh.biz.xs.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lab.s2jh.biz.xs.dao.XsJbxxDao;
import lab.s2jh.biz.xs.entity.XsJbxx;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class XsJbxxService extends BaseService<XsJbxx, String> {

    @Autowired
    private XsJbxxDao xsJbxxDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AclService aclService;

    @Override
    protected BaseDao<XsJbxx, String> getEntityDao() {
        return xsJbxxDao;
    }

    @Transactional(readOnly = true)
    public List<XsJbxx> findByXhStartingWith(String xxdm, String xhPrefix) {
        return xsJbxxDao.findByXxdmAndXhStartingWith(xxdm, xhPrefix);
    }

    @Transactional(readOnly = true)
    public XsJbxx findByXxdmAndXh(String xxdm, String xh) {
        return xsJbxxDao.findByXxdmAndXh(xxdm, xh);
    }
}
