package lab.s2jh.profile.dao;

import java.util.List;

import lab.s2jh.auth.entity.User;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.profile.entity.SimpleParamVal;

import org.springframework.stereotype.Repository;

@Repository
public interface SimpleParamValDao extends BaseDao<SimpleParamVal, String> {
    SimpleParamVal findByUserAndCode(User user, String code);

    List<SimpleParamVal> findByUser(User user);
}