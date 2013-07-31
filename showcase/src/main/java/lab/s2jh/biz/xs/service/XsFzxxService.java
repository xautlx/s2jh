package lab.s2jh.biz.xs.service;

import lab.s2jh.biz.xs.dao.XsFzxxDao;
import lab.s2jh.biz.xs.entity.XsFzxx;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class XsFzxxService extends BaseService<XsFzxx,String>{
    
    @Autowired
    private XsFzxxDao xsFzxxDao;

    @Override
    protected BaseDao<XsFzxx, String> getEntityDao() {
        return xsFzxxDao;
    }
}
