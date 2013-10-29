package lab.s2jh.core.audit.envers;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lab.s2jh.core.web.json.DateTimeJsonSerializer;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 扩展默认的Hibernate Envers审计表对象定义
 * 
 * @see http://docs.jboss.org/hibernate/orm/4.2/devguide/en-US/html/ch15.html
 */
@Entity
@Table(name = "T_AUD_REVINFO")
@RevisionEntity(ExtRevisionListener.class)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "javassistLazyInitializer", "revisionEntity", "handler" }, ignoreUnknown = true)
public class ExtDefaultRevisionEntity {
    /** 记录版本 */
    private Long rev;
    /** 记录时间 */
    private Date revstmp;
    /** 登录用户账号信息，一般用于页面友好显示 */
    private String username;
    /** 全局唯一的用户ID，确保明确与唯一操作用户关联 */
    private String uid;
    /** 基于ThreadLocal方式记录前端Web界面用户填写的操作说明 */
    private String operationExplain;  
    /** 基于ThreadLocal方式记录操作事件记录 */
    private String operationEvent;
    /** 基于ThreadLocal方式记录对象旧状态 */
    private String oldState;
    /** 基于ThreadLocal方式记录对象新状态 */
    private String newState;
    
    /** 辅助属性:显示数据对应中文描述文本 */
    private String operationEventDisplay;
    /** 辅助属性:显示数据对应中文描述文本 */
    private String oldStateDisplay;
    /** 辅助属性:显示数据对应中文描述文本 */
    private String newStateDisplay;

    @Id
    @GeneratedValue
    @RevisionNumber
    public Long getRev() {
        return rev;
    }

    public void setRev(Long rev) {
        this.rev = rev;
    }

    @RevisionTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getRevstmp() {
        return revstmp;
    }

    public void setRevstmp(Date revstmp) {
        this.revstmp = revstmp;
    }

    


    @Column(length = 128, name = "user_id")
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Column(length = 50, name = "user_name")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(length = 512)
    public String getOperationExplain() {
        return operationExplain;
    }

    public void setOperationExplain(String operationExplain) {
        this.operationExplain = operationExplain;
    }

    @Column(length = 32)
    public String getOperationEvent() {
        return operationEvent;
    }

    public void setOperationEvent(String operationEvent) {
        this.operationEvent = operationEvent;
    }

    @Column(length = 32)
    public String getOldState() {
        return oldState;
    }

    public void setOldState(String oldState) {
        this.oldState = oldState;
    }

    @Column(length = 32)
    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }

    @Transient
    public String getOperationEventDisplay() {
        return operationEventDisplay;
    }

    public void setOperationEventDisplay(String operationEventDisplay) {
        this.operationEventDisplay = operationEventDisplay;
    }

    @Transient
    public String getOldStateDisplay() {
        return oldStateDisplay;
    }

    public void setOldStateDisplay(String oldStateDisplay) {
        this.oldStateDisplay = oldStateDisplay;
    }

    @Transient
    public String getNewStateDisplay() {
        return newStateDisplay;
    }

    public void setNewStateDisplay(String newStateDisplay) {
        this.newStateDisplay = newStateDisplay;
    }
}
