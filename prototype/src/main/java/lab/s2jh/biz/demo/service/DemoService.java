package lab.s2jh.biz.demo.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.biz.demo.entity.Demo;
import lab.s2jh.biz.demo.dao.DemoDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DemoService extends BaseService<Demo,String>{
    
    @Autowired
    private DemoDao demoDao;

    @Override
    protected BaseDao<Demo, String> getEntityDao() {
        return demoDao;
    }
}
