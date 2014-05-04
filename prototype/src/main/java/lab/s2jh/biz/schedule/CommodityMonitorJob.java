package lab.s2jh.biz.schedule;

import lab.s2jh.schedule.BaseQuartzJobBean;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 商品数据监控预警任务
 */
public class CommodityMonitorJob extends BaseQuartzJobBean {

    private final static Logger logger = LoggerFactory.getLogger(CommodityMonitorJob.class);

    @Override
    protected void executeInternalBiz(JobExecutionContext context) {
        logger.debug("Just Mock: Monitor commodity data...");
    }

}
