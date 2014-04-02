package lab.s2jh.auth.dao;

import lab.s2jh.auth.entity.SignupUser;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface SignupUserDao extends BaseDao<SignupUser, String> {

}