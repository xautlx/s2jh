package lab.s2jh.profile.service;

import java.util.List;

import lab.s2jh.auth.entity.User;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.profile.dao.SimpleParamValDao;
import lab.s2jh.profile.entity.SimpleParamVal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SimpleParamValService extends BaseService<SimpleParamVal, String> {

    @Autowired
    private SimpleParamValDao simpleParamValDao;

    @Override
    protected BaseDao<SimpleParamVal, String> getEntityDao() {
        return simpleParamValDao;
    }

    public SimpleParamVal findByUserAndCode(User user, String code) {
        return simpleParamValDao.findByUserAndCode(user, code);
    }

    public List<SimpleParamVal> findByUser(User user) {
        return simpleParamValDao.findByUser(user);
    }
}
