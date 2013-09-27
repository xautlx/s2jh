package lab.s2jh.biz.xs.job;

import lab.s2jh.schedule.BaseQuartzJobBean;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模拟定时学生信息查重计划作业
 */
public class DupStatJob extends BaseQuartzJobBean{
    
    private final static Logger logger = LoggerFactory.getLogger(DupStatJob.class);

    @Override
    protected void executeInternalBiz(JobExecutionContext context) {
        logger.debug("Mock executing XSXX duplication check job...");
    }

}
