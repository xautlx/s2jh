package lab.s2jh.rpt.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.rpt.entity.ReportParam;
import lab.s2jh.rpt.dao.ReportParamDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportParamService extends BaseService<ReportParam,String>{
    
    @Autowired
    private ReportParamDao reportParamDao;

    @Override
    protected BaseDao<ReportParam, String> getEntityDao() {
        return reportParamDao;
    }
}
