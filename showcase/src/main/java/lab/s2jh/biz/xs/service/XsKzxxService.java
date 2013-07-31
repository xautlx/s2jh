package lab.s2jh.biz.xs.service;

import lab.s2jh.biz.xs.dao.XsKzxxDao;
import lab.s2jh.biz.xs.entity.XsKzxx;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class XsKzxxService extends BaseService<XsKzxx,String>{
    
    @Autowired
    private XsKzxxDao xsKzxxDao;

    @Override
    protected BaseDao<XsKzxx, String> getEntityDao() {
        return xsKzxxDao;
    }
}
