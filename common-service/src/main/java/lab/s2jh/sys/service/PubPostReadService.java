package lab.s2jh.sys.service;

import java.util.List;

import lab.s2jh.auth.entity.User;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.sys.dao.PubPostReadDao;
import lab.s2jh.sys.entity.PubPost;
import lab.s2jh.sys.entity.PubPostRead;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PubPostReadService extends BaseService<PubPostRead,String>{
    
    @Autowired
    private PubPostReadDao pubPostReadDao;

    @Override
    protected BaseDao<PubPostRead, String> getEntityDao() {
        return pubPostReadDao;
    }
    
    public List<PubPostRead> findReaded(User readUser,List<PubPost> pubPosts){
        return pubPostReadDao.findReaded(readUser, pubPosts);
    }
    
    public PubPostRead findReaded(User readUser,PubPost pubPost){
        return pubPostReadDao.findByReadUserAndPubPost(readUser, pubPost);
    }
}
