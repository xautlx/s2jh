package lab.s2jh.schedule;

import java.util.List;

import lab.s2jh.schedule.entity.JobBeanCfg;
import lab.s2jh.schedule.service.JobBeanCfgService;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.google.common.collect.Lists;

/**
 * 扩展标准的SchedulerFactoryBean，实现基于数据库配置的任务管理器初始化
 */
public class ExtSchedulerFactoryBean extends SchedulerFactoryBean {

    private static Logger logger = LoggerFactory.getLogger(ExtSchedulerFactoryBean.class);

    private ConfigurableApplicationContext applicationContext;

    private JobBeanCfgService jobBeanCfgService;

    private Boolean runWithinCluster = Boolean.TRUE;

    public void setRunWithinCluster(Boolean runWithinCluster) {
        this.runWithinCluster = runWithinCluster;
    }

    public Boolean getRunWithinCluster() {
        return runWithinCluster;
    }

    public void setJobBeanCfgService(JobBeanCfgService jobBeanCfgService) {
        this.jobBeanCfgService = jobBeanCfgService;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        super.setApplicationContext(applicationContext);
    }

    @Override
    protected void registerJobsAndTriggers() throws SchedulerException {
        logger.debug("Invoking registerJobsAndTriggers...");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
        List<JobBeanCfg> jobBeanCfgs = jobBeanCfgService.findAll();
        List<Trigger> allTriggers = Lists.newArrayList();

        List<Trigger> triggers = null;
        try {
            //基于反射获取已经在XML中定义的triggers集合
            triggers = (List<Trigger>) FieldUtils.readField(this, "triggers", true);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }

        if (triggers == null) {
            triggers = Lists.newArrayList();
        } else {
            allTriggers.addAll(triggers);
        }

        for (JobBeanCfg jobBeanCfg : jobBeanCfgs) {
            // 只处理与当前Scheduler集群运行模式匹配的数据
            if (jobBeanCfg.getRunWithinCluster() == null || !jobBeanCfg.getRunWithinCluster().equals(runWithinCluster)) {
                continue;
            }
            // 以任务全类名作为Job和Trigger相关名称
            Class<?> jobClass = null;
            try {
                jobClass = Class.forName(jobBeanCfg.getJobClass());
            } catch (ClassNotFoundException e) {
                //容错处理避免由于配置错误导致无法启动应用
                logger.error(e.getMessage(), e);
            }
            if (jobClass == null) {
                continue;
            }
            String jobName = jobClass.getName();

            boolean jobExists = false;
            for (Trigger trigger : triggers) {
                if (trigger.getJobKey().getName().equals(jobName)) {
                    jobExists = true;
                    break;
                }
            }
            if (jobExists) {
                logger.warn("WARN: Skipped dynamic  job [{}] due to exists static configuration.", jobName);
                continue;
            }

            logger.debug("Build and schedule dynamical job： {}, CRON: {}", jobName, jobBeanCfg.getCronExpression());

            // Spring动态加载Job Bean
            BeanDefinitionBuilder bdbJobDetailBean = BeanDefinitionBuilder
                    .rootBeanDefinition(JobDetailFactoryBean.class);
            bdbJobDetailBean.addPropertyValue("jobClass", jobBeanCfg.getJobClass());
            bdbJobDetailBean.addPropertyValue("durability", true);
            beanFactory.registerBeanDefinition(jobName, bdbJobDetailBean.getBeanDefinition());

            // Spring动态加载Trigger Bean
            String triggerName = jobName + ".Trigger";
            JobDetail jobDetailBean = (JobDetail) beanFactory.getBean(jobName);
            BeanDefinitionBuilder bdbCronTriggerBean = BeanDefinitionBuilder
                    .rootBeanDefinition(CronTriggerFactoryBean.class);
            bdbCronTriggerBean.addPropertyValue("jobDetail", jobDetailBean);
            bdbCronTriggerBean.addPropertyValue("cronExpression", jobBeanCfg.getCronExpression());
            beanFactory.registerBeanDefinition(triggerName, bdbCronTriggerBean.getBeanDefinition());

            allTriggers.add((Trigger) beanFactory.getBean(triggerName));
        }

        this.setTriggers(allTriggers.toArray(new Trigger[] {}));
        super.registerJobsAndTriggers();

        // 把AutoStartup设定的计划任务初始设置为暂停状态
        for (JobBeanCfg jobBeanCfg : jobBeanCfgs) {
            if (!jobBeanCfg.getAutoStartup()) {
                for (Trigger trigger : allTriggers) {
                    if (jobBeanCfg.getJobClass().equals(trigger.getJobKey().getName())) {
                        logger.debug("Setup trigger {} state to PAUSE", trigger.getKey().getName());
                        this.getScheduler().pauseTrigger(trigger.getKey());
                        break;
                    }
                }
            }
        }
    }

}
