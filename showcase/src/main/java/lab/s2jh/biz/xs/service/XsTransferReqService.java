package lab.s2jh.biz.xs.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lab.s2jh.biz.xs.dao.XsJbxxDao;
import lab.s2jh.biz.xs.dao.XsTransferReqDao;
import lab.s2jh.biz.xs.entity.XsJbxx;
import lab.s2jh.biz.xs.entity.XsTransferReq;
import lab.s2jh.biz.xx.entity.XxBj;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class XsTransferReqService extends BaseService<XsTransferReq, String> {

    @Autowired
    private XsTransferReqDao xsTransferReqDao;
    
    @Autowired
    private XsJbxxDao xsJbxxDao;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected BaseDao<XsTransferReq, String> getEntityDao() {
        return xsTransferReqDao;
    }
    
    /**
     * 学籍转入
     * @param xxBj 新班级
     * @param newXh 新学号
     * @param entity
     * @param xsTransferReqLog
     * @return
     */
    public XsJbxx transferIn(XxBj xxBj,String newXh,XsTransferReq entity){
        
        
        XsJbxx xsJbxx=entity.getXsJbxx();
        entityManager.detach(xsJbxx);
        xsJbxx.setId(null);
        xsJbxx.setXh(newXh);
        xsJbxx.setXxBj(xxBj);
        xsJbxx.setXxdm(xxBj.getXxdm());
        xsJbxxDao.save(xsJbxx);
        
        entity.setNewXsJbxx(xsJbxx);
        super.save(entity);
        
        return xsJbxx;
    }
}
