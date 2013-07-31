package lab.s2jh.auth.service;

import lab.s2jh.auth.dao.UserLogonLogDao;
import lab.s2jh.auth.entity.UserLogonLog;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserLogonLogService extends BaseService<UserLogonLog,String>{
    
    @Autowired
    private UserLogonLogDao userLogonLogDao;

    @Override
    protected BaseDao<UserLogonLog, String> getEntityDao() {
        return userLogonLogDao;
    }
    
    public UserLogonLog findBySessionId(String httpSessionId){
        return userLogonLogDao.findByHttpSessionId(httpSessionId);
    }
}
