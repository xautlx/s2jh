package lab.s2jh.schedule.dao;

import lab.s2jh.schedule.entity.JobRunHist;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface JobRunHistDao extends BaseDao<JobRunHist, String> {

}