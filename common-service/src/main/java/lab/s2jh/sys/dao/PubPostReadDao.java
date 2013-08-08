package lab.s2jh.sys.dao;

import java.util.List;

import lab.s2jh.auth.entity.User;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.sys.entity.PubPost;
import lab.s2jh.sys.entity.PubPostRead;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PubPostReadDao extends BaseDao<PubPostRead, String> {

    @Query("from PubPostRead t where t.readUser=:readUser and t.pubPost in (:pubPosts)")
    List<PubPostRead> findReaded(@Param("readUser") User readUser, @Param("pubPosts") List<PubPost> pubPosts);

    PubPostRead findByReadUserAndPubPost(User readUser, PubPost pubPost);
}