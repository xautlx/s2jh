package lab.s2jh.biz.xs.service;

import lab.s2jh.biz.xs.dao.XsLxxxDao;
import lab.s2jh.biz.xs.entity.XsLxxx;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class XsLxxxService extends BaseService<XsLxxx,String>{
    
    @Autowired
    private XsLxxxDao xsLxxxDao;

    @Override
    protected BaseDao<XsLxxx, String> getEntityDao() {
        return xsLxxxDao;
    }
}
