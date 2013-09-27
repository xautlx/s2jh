package lab.s2jh.schedule.job;

import lab.s2jh.schedule.BaseQuartzJobBean;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseStatJob extends BaseQuartzJobBean {

    private final static Logger logger = LoggerFactory.getLogger(DatabaseStatJob.class);

    @Override
    protected void executeInternalBiz(JobExecutionContext context) {
        logger.debug("Just Mock: Stat database information running with Spring Quartz cluster mode...");
    }

}
