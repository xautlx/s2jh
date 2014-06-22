package lab.s2jh.biz.finance.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.biz.finance.entity.BizTradeUnit;
import lab.s2jh.biz.finance.dao.BizTradeUnitDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BizTradeUnitService extends BaseService<BizTradeUnit,Long>{
    
    @Autowired
    private BizTradeUnitDao bizTradeUnitDao;

    @Override
    protected BaseDao<BizTradeUnit, Long> getEntityDao() {
        return bizTradeUnitDao;
    }
}
