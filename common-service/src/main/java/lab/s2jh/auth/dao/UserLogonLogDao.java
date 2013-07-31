package lab.s2jh.auth.dao;

import lab.s2jh.auth.entity.UserLogonLog;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface UserLogonLogDao extends BaseDao<UserLogonLog, String> {

    UserLogonLog findByHttpSessionId(String httpSessionId);
}