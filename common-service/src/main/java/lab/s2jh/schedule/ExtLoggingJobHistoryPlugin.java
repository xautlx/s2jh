package lab.s2jh.schedule;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

import lab.s2jh.core.context.SpringContextHolder;
import lab.s2jh.schedule.entity.JobBeanCfg;
import lab.s2jh.schedule.entity.JobRunHist;
import lab.s2jh.schedule.service.JobBeanCfgService;
import lab.s2jh.schedule.service.JobRunHistService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.plugins.history.LoggingJobHistoryPlugin;

import com.google.common.collect.Sets;

/**
 * 扩展实现LoggingJobHistoryPlugin约定接口
 * 转换Quartz提供的相关接口数据为ScheduleJobRunHist对象并调用对应的Service接口把数据写入数据库表中
 */
public class ExtLoggingJobHistoryPlugin extends LoggingJobHistoryPlugin {

	public static Set<Trigger> UNCONFIG_TRIGGERS = Sets.newHashSet();

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		super.jobWasExecuted(context, jobException);

		Trigger trigger = context.getTrigger();
		if (!UNCONFIG_TRIGGERS.contains(trigger)) {
			JobBeanCfgService jobBeanCfgService = SpringContextHolder.getBean(JobBeanCfgService.class);
			JobBeanCfg jobBeanCfg = jobBeanCfgService.findByJobClass(trigger.getJobKey().getName());
			if (jobBeanCfg == null) {
				UNCONFIG_TRIGGERS.add(trigger);
			} else {
				if (!jobBeanCfg.getLogRunHist()) {
					return;
				}
			}
		}

		JobRunHistService jobRunHistService = SpringContextHolder.getBean(JobRunHistService.class);
		JobRunHist jobRunHist = new JobRunHist();
		try {
			jobRunHist.setNodeId(InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			jobRunHist.setNodeId("U/A");
		}
		jobRunHist.setTriggerGroup(trigger.getKey().getGroup());
		jobRunHist.setTriggerName(trigger.getKey().getName());
		jobRunHist.setJobClass(context.getJobDetail().getJobClass().getName());
		jobRunHist.setJobName(context.getJobDetail().getKey().getName());
		jobRunHist.setJobGroup(context.getJobDetail().getKey().getGroup());
		jobRunHist.setFireTime(new java.util.Date());
		jobRunHist.setPreviousFireTime(trigger.getPreviousFireTime());
		jobRunHist.setNextFireTime(trigger.getNextFireTime());
		jobRunHist.setRefireCount(new Integer(context.getRefireCount()));
		if (jobException != null) {
			jobRunHist.setExceptionFlag(Boolean.TRUE);
			jobRunHist.setResult(jobException.getMessage());
			StringWriter strWriter = new StringWriter();
			PrintWriter writer = new PrintWriter(new BufferedWriter(strWriter));
			jobException.printStackTrace(writer);
			writer.flush();
			strWriter.flush();
			String exceptionStack = strWriter.getBuffer().toString();
			jobRunHist.setExceptionStack(exceptionStack);
		} else {
			jobRunHist.setExceptionFlag(Boolean.FALSE);
			if (context.getResult() == null) {
				jobRunHist.setResult("SUCCESS");
			} else {
				String result = String.valueOf(context.getResult());
				jobRunHist.setResult(result);
			}
		}
		jobRunHistService.saveWithAsyncAndNewTransition(jobRunHist);
	}

}