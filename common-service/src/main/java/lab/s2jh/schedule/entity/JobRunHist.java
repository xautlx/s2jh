package lab.s2jh.schedule.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 任务计划运行历史记录
 */
@MetaData(value = "定时任务运行记录")
@Entity
@Table(name = "T_JOB_RUN_HIST")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JobRunHist extends BaseEntity<String> {

    @MetaData(value = "Job名称")
    @EntityAutoCode(order = 10, listShow = false)
    private String jobName;

    @MetaData(value = "Job分组")
    @EntityAutoCode(order = 20, listShow = false)
    private String jobGroup;

    @MetaData(value = "Job类")
    @EntityAutoCode(order = 25)
    private String jobClass;

    @MetaData(value = "Trigger名称")
    @EntityAutoCode(order = 30, listShow = false)
    private String triggerName;

    @MetaData(value = "Trigger分组 ")
    @EntityAutoCode(order = 40, listShow = false)
    private String triggerGroup;
    
    @MetaData(value = "异常标识")
    @EntityAutoCode(order = 90)
    private Boolean exceptionFlag = Boolean.FALSE;

    @MetaData(value = "执行结果")
    @EntityAutoCode(order = 100)
    private String result;

    @MetaData(value = "异常日志")
    @EntityAutoCode(order = 110, listShow = false)
    private String exceptionStack;

    //以下参数具体参考官方接口文档说明：
    //org.quartz.plugins.history.LoggingJobHistoryPlugin.LoggingJobHistoryPlugin#jobWasExecuted(JobExecutionContext context, JobExecutionException jobException)
    @MetaData(value = "本次触发时间")
    @EntityAutoCode(order = 50)
    private Date fireTime;

    @EntityAutoCode(order = 60)
    @MetaData(value = "上次触发时间")
    private Date previousFireTime;

    @EntityAutoCode(order = 70)
    @MetaData(value = "下次触发时间")
    private Date nextFireTime;

    @MetaData(value = "触发次数")
    @EntityAutoCode(order = 80)
    private Integer refireCount;
    
    @MetaData(value = "触发节点标识")
    @EntityAutoCode(order = 100)
    private String nodeId;

    private String id;

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Column(length = 64, nullable = true)
    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    @Column(length = 64, nullable = true)
    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    @Column(length = 64, nullable = true)
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Column(length = 64, nullable = true)
    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    public Date getPreviousFireTime() {
        return previousFireTime;
    }

    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Integer getRefireCount() {
        return refireCount;
    }

    public void setRefireCount(Integer refireCount) {
        this.refireCount = refireCount;
    }

    @Type(type = "yes_no")
    @Column(nullable = true)
    public Boolean getExceptionFlag() {
        return exceptionFlag;
    }

    public void setExceptionFlag(Boolean exceptionFlag) {
        this.exceptionFlag = exceptionFlag;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String getExceptionStack() {
        return exceptionStack;
    }

    public void setExceptionStack(String exceptionStack) {
        this.exceptionStack = exceptionStack;
    }

    @Override
    @Transient
    public String getDisplayLabel() {
        return jobClass;
    }

    @Column(length = 512, nullable = true)
    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
