package lab.s2jh.schedule.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.schedule.entity.JobBeanCfg;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface JobBeanCfgDao extends BaseDao<JobBeanCfg, String> {

    @Query("from JobBeanCfg")
    List<JobBeanCfg> findAll();
    
    @QueryHints({ @QueryHint(name = org.hibernate.ejb.QueryHints.HINT_CACHEABLE, value = "true") })
    JobBeanCfg findByJobClass(String jobClass);
}