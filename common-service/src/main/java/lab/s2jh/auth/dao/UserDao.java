package lab.s2jh.auth.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.auth.entity.User;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseDao<User, String> {

    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    User findByAclCodeAndSigninid(String aclCode, String signid);

    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    List<User> findBySigninid(String signid);

    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    User findByInitSetupUser(Boolean initSetupUser);

    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    User findByUid(String uid);
}
