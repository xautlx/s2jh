package lab.s2jh.biz.finance.service;

import java.util.List;

import lab.s2jh.biz.finance.dao.BizTradeUnitDao;
import lab.s2jh.biz.finance.entity.BizTradeUnit;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional
public class BizTradeUnitService extends BaseService<BizTradeUnit, Long> {

    @Autowired
    private BizTradeUnitDao bizTradeUnitDao;

    @Override
    protected BaseDao<BizTradeUnit, Long> getEntityDao() {
        return bizTradeUnitDao;
    }

    /**
     * 查询常用数据用于缓存
     * @return
     */
    public List<BizTradeUnit> findFrequentUsedDatas() {
        //TODO：可优化为基于常用采购/销售等实际发生的业务数据关联获取
        return Lists.newArrayList(bizTradeUnitDao.findAll());
    }
}
