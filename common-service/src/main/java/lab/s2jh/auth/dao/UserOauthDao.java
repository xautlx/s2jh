package lab.s2jh.auth.dao;

import javax.persistence.QueryHint;

import lab.s2jh.auth.entity.UserOauth;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOauthDao extends BaseDao<UserOauth, String> {

	@QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
	UserOauth findByUsername(String username);
}
