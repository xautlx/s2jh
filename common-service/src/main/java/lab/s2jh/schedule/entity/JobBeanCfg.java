package lab.s2jh.schedule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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

@MetaData(value = "定时任务配置")
@Entity
@Table(name = "tbl_JOB_BEAN_CFG")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JobBeanCfg extends BaseEntity<String> {

    @MetaData(value = "任务类全名", description = "实现QuartzJobBean的类全路径类名 ")
    @EntityAutoCode(order = 5)
    private String jobClass;

    @MetaData(value = "CRON表达式", description = "定时表达式，基于CRON语法")
    @EntityAutoCode(order = 10)
    private String cronExpression;

    @MetaData(value = "自动初始运行")
    @EntityAutoCode(order = 20)
    private Boolean autoStartup;

    @MetaData(value = "启用运行记录",description="每次运行会写入历史记录表，对于运行频率很高或业务监控意义不大的任务建议关闭")
    @EntityAutoCode(order = 30)
    private Boolean logRunHist;
    
    @MetaData(value = "集群运行模式",description="如果为true，则在一组集群部署环境中同一任务只会在一个节点触发；否则在每个节点各自独立运行")
    @EntityAutoCode(order = 40)
    private Boolean runWithinCluster;

    @MetaData(value = "描述")
    @EntityAutoCode(order = 50, listShow = false)
    private String description;
    
    @MetaData(value = "结果模板文本")
    private String resultTemplate;

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

    @Column(length = 512, nullable = false, unique = true)
    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    @Column(length = 64, nullable = false)
    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Column(length = 1000, nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAutoStartup() {
        return autoStartup;
    }

    public void setAutoStartup(Boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    @Override
    @Transient
    public String getDisplay() {
        return jobClass + ":" + cronExpression;
    }

    public Boolean getLogRunHist() {
        return logRunHist;
    }

    public void setLogRunHist(Boolean logRunHist) {
        this.logRunHist = logRunHist;
    }

    public Boolean getRunWithinCluster() {
        return runWithinCluster;
    }

    public void setRunWithinCluster(Boolean runWithinCluster) {
        this.runWithinCluster = runWithinCluster;
    }

    @Lob
	public String getResultTemplate() {
		return resultTemplate;
	}

	public void setResultTemplate(String resultTemplate) {
		this.resultTemplate = resultTemplate;
	}
}
