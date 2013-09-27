package lab.s2jh.schedule.service;

import java.util.List;
import java.util.Map;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.exception.ServiceException;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.schedule.ExtSchedulerFactoryBean;
import lab.s2jh.schedule.dao.JobBeanCfgDao;
import lab.s2jh.schedule.entity.JobBeanCfg;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

@Service
@Transactional
public class JobBeanCfgService extends BaseService<JobBeanCfg, String> {

    private static Logger logger = LoggerFactory.getLogger(JobBeanCfgService.class);

    @Autowired
    private JobBeanCfgDao jobBeanCfgDao;

    @Autowired(required = false)
    @Qualifier("quartzRAMScheduler")
    private SchedulerFactoryBean quartzRAMScheduler;

    @Autowired(required = false)
    @Qualifier("quartzClusterScheduler")
    private SchedulerFactoryBean quartzClusterScheduler;

    @Override
    protected BaseDao<JobBeanCfg, String> getEntityDao() {
        return jobBeanCfgDao;
    }

    public List<JobBeanCfg> findAll() {
        return jobBeanCfgDao.findAll();
    }
    
    
    public JobBeanCfg findByJobClass(String jobClass){
        return jobBeanCfgDao.findByJobClass(jobClass);
    }

    @SuppressWarnings({ "unused", "unchecked" })
    public Map<Trigger, SchedulerFactoryBean> findAllTriggers() {
        Map<Trigger, SchedulerFactoryBean> allTriggers = Maps.newLinkedHashMap();
        try {
            if (quartzRAMScheduler != null) {
                Scheduler scheduler = quartzRAMScheduler.getScheduler();
                for (Trigger trigger : (List<Trigger>) FieldUtils.readField(quartzRAMScheduler, "triggers", true)) {
                    allTriggers.put(trigger, quartzRAMScheduler);
                }
            }

            if (quartzClusterScheduler != null) {
                Scheduler scheduler = quartzClusterScheduler.getScheduler();
                for (Trigger trigger : (List<Trigger>) FieldUtils.readField(quartzClusterScheduler, "triggers", true)) {
                    allTriggers.put(trigger, quartzClusterScheduler);
                }
            }
        } catch (Exception e) {
            throw new ServiceException("Quartz trigger schedule error", e);
        }
        return allTriggers;
    }

    @Override
    public JobBeanCfg save(JobBeanCfg entity) {
        try {
            if (!entity.isNew()) {//新配置任务不做Schedule处理，需要重新启动应用服务器才能生效
                Map<Trigger, SchedulerFactoryBean> allTriggers = findAllTriggers();
                for (Map.Entry<Trigger, SchedulerFactoryBean> me : allTriggers.entrySet()) {
                    CronTrigger cronTrigger = (CronTrigger) me.getKey();
                    ExtSchedulerFactoryBean schedulerFactoryBean = (ExtSchedulerFactoryBean) me.getValue();
                    Scheduler scheduler = schedulerFactoryBean.getScheduler();
                    if (cronTrigger.getJobName().equals(entity.getJobClass())
                            && !entity.getCronExpression().equals(cronTrigger.getCronExpression())) {
                        String oldCronExpression = cronTrigger.getCronExpression();
                        cronTrigger.setCronExpression(entity.getCronExpression());
                        logger.info("RescheduleJob : {}, CRON from {} to {}", cronTrigger.getFullJobName(),
                                oldCronExpression, cronTrigger.getCronExpression());
                        scheduler.rescheduleJob(cronTrigger.getName(), cronTrigger.getGroup(), cronTrigger);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException("Quartz trigger schedule error", e);
        }
        return super.save(entity);
    }

    @Override
    public void delete(JobBeanCfg entity) {
        try {
            Map<Trigger, SchedulerFactoryBean> allTriggers = findAllTriggers();
            for (Map.Entry<Trigger, SchedulerFactoryBean> me : allTriggers.entrySet()) {
                CronTrigger cronTrigger = (CronTrigger) me.getKey();
                ExtSchedulerFactoryBean schedulerFactoryBean = (ExtSchedulerFactoryBean) me.getValue();
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                if (cronTrigger.getJobName().equals(entity.getJobClass())) {
                    logger.info("UnscheduleJob from quartzClusterScheduler: {}", cronTrigger.getFullJobName());
                    scheduler.unscheduleJob(cronTrigger.getName(), cronTrigger.getGroup());
                    break;
                }
            }
        } catch (Exception e) {
            throw new ServiceException("Quartz trigger schedule error", e);
        }
        super.delete(entity);
    }

}
