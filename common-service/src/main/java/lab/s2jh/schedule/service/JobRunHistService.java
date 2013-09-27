package lab.s2jh.schedule.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.schedule.dao.JobRunHistDao;
import lab.s2jh.schedule.entity.JobRunHist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobRunHistService extends BaseService<JobRunHist,String>{
    
    @Autowired
    private JobRunHistDao jobRunHistDao;

    @Override
    protected BaseDao<JobRunHist, String> getEntityDao() {
        return jobRunHistDao;
    }
    
    @Async
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void saveWithAsyncAndNewTransition(JobRunHist entity) {
        jobRunHistDao.save(entity);
    }
}
