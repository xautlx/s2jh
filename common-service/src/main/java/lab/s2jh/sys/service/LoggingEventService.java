package lab.s2jh.sys.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.sys.dao.LoggingEventDao;
import lab.s2jh.sys.entity.LoggingEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoggingEventService extends BaseService<LoggingEvent,Long>{
    
    @Autowired
    private LoggingEventDao loggingEventDao;

    @Override
    protected BaseDao<LoggingEvent, Long> getEntityDao() {
        return loggingEventDao;
    }
}
