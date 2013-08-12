package lab.s2jh.sys.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.audit.SaveUpdateAuditListener;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.entity.annotation.SkipParamBind;
import lab.s2jh.core.entity.def.DefaultAuditable;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/** 
 * @see http://logback.qos.ch/manual/configuration.html #DBAppender
 */
@Entity
@Table(name = "logging_event")
@Cache(usage = CacheConcurrencyStrategy.NONE)
@EntityListeners({ SaveUpdateAuditListener.class })
@MetaData(title = "日志事件", description = "用于基于Logback日志DBAppender的ERROR日志数据存取")
public class LoggingEvent extends PersistableEntity<Long> implements DefaultAuditable<String, Long> {

    private Long id;
    private Long timestmp;
    private String formattedMessage;
    private String loggerName;
    private String levelString;
    private String threadName;
    private Integer referenceFlag;
    private String arg0;
    private String arg1;
    private String arg2;
    private String arg3;
    private String callerFilename;
    private String callerClass;
    private String callerMethod;
    private String callerLine;
    private List<LoggingEventProperty> loggingEventProperties;
    private List<LoggingEventException> loggingEventExceptions;

    private LoggingHandleStateEnum state = LoggingHandleStateEnum.TBD;
    private String operationExplain;

    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;

    public static enum LoggingHandleStateEnum {
        TBD, FIXED, TODO, CANCELED, IGNORE;
    }

    @Id
    @Column(name = "event_id")
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "native")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Long timestmp) {
        this.timestmp = timestmp;
    }

    @Column(length = 4000)
    public String getFormattedMessage() {
        return formattedMessage;
    }

    public void setFormattedMessage(String formattedMessage) {
        this.formattedMessage = formattedMessage;
    }

    @Column(length = 256)
    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    @Column(length = 256)
    public String getLevelString() {
        return levelString;
    }

    public void setLevelString(String levelString) {
        this.levelString = levelString;
    }

    @Column(length = 256)
    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public Integer getReferenceFlag() {
        return referenceFlag;
    }

    public void setReferenceFlag(Integer referenceFlag) {
        this.referenceFlag = referenceFlag;
    }

    @Column(length = 256)
    public String getArg0() {
        return arg0;
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    @Column(length = 256)
    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    @Column(length = 256)
    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    @Column(length = 256)
    public String getArg3() {
        return arg3;
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }

    @Column(length = 256)
    public String getCallerFilename() {
        return callerFilename;
    }

    public void setCallerFilename(String callerFilename) {
        this.callerFilename = callerFilename;
    }

    @Column(length = 256)
    public String getCallerClass() {
        return callerClass;
    }

    public void setCallerClass(String callerClass) {
        this.callerClass = callerClass;
    }

    @Column(length = 256)
    public String getCallerMethod() {
        return callerMethod;
    }

    public void setCallerMethod(String callerMethod) {
        this.callerMethod = callerMethod;
    }

    @Column(length = 8)
    public String getCallerLine() {
        return callerLine;
    }

    public void setCallerLine(String callerLine) {
        this.callerLine = callerLine;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "loggingEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<LoggingEventProperty> getLoggingEventProperties() {
        return loggingEventProperties;
    }

    public void setLoggingEventProperties(List<LoggingEventProperty> loggingEventProperties) {
        this.loggingEventProperties = loggingEventProperties;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "loggingEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<LoggingEventException> getLoggingEventExceptions() {
        return loggingEventExceptions;
    }

    public void setLoggingEventExceptions(List<LoggingEventException> loggingEventExceptions) {
        this.loggingEventExceptions = loggingEventExceptions;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = true)
    public LoggingHandleStateEnum getState() {
        return state;
    }

    public void setState(LoggingHandleStateEnum state) {
        this.state = state;
    }

    @Column(length = 4000)
    @JsonIgnore
    public String getOperationExplain() {
        return operationExplain;
    }

    public void setOperationExplain(String operationExplain) {
        this.operationExplain = operationExplain;
    }

    @Transient
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getTimestampDate() {
        return new Date(this.getTimestmp());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Auditable#getCreatedBy()
     */
    @JsonIgnore
    @Column(updatable = false)
    public String getCreatedBy() {
        return createdBy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.domain.Auditable#setCreatedBy(java.lang.Object)
     */
    @SkipParamBind
    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getCreatedDate() {
        return createdDate;
    }

    @SkipParamBind
    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Auditable#getLastModifiedBy()
     */
    @JsonIgnore
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    @SkipParamBind
    public void setLastModifiedBy(final String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @JsonIgnore
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(final Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Transient
    @JsonIgnore
    public String getExceptionStack() {
        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(loggingEventExceptions)) {
            for (LoggingEventException loggingEventException : loggingEventExceptions) {
                sb.append(loggingEventException.getTraceLine() + "\n");
            }
        }
        return sb.toString();
    }

    @Override
    @Transient
    public String getDisplayLabel() {
        return formattedMessage;
    }

}
