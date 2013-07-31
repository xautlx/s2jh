package lab.s2jh.biz.xx.service;

import java.util.List;

import lab.s2jh.biz.xx.dao.XxBjDao;
import lab.s2jh.biz.xx.entity.XxBj;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class XxBjService extends BaseService<XxBj, String> {

    @Autowired
    private XxBjDao xxBjDao;

    @Override
    protected BaseDao<XxBj, String> getEntityDao() {
        return xxBjDao;
    }

    @Transactional(readOnly = true)
    public List<XxBj> findByXxdm(String xxdm, String nj) {
        return xxBjDao.findByXxdmAndNj(xxdm, nj);
    }

    @Transactional(readOnly = true)
    public XxBj findByXxdmAndBh(String xxdm, String bh) {
        if (StringUtils.isBlank(xxdm)) {
            return xxBjDao.findByBh(bh);
        } else {
            return xxBjDao.findByXxdmAndBh(xxdm, bh);
        }

    }

    public List<XxBj> findByXxdmAndBhStartingWithOrBjmcLike(String xxdm, String term) {
        if (StringUtils.isBlank(xxdm)) {
            return xxBjDao.findByBhOrBjmcLike(term + "%", "%" + term + "%");
        } else {
            return xxBjDao.findByXxdmAndBhOrBjmcLike(xxdm, term + "%", "%" + term + "%");
        }
    }

}
