package lab.s2jh.sys.dao;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.sys.entity.LoggingEvent;

import org.springframework.stereotype.Repository;

@Repository
public interface LoggingEventDao extends BaseDao<LoggingEvent, Long> {

}