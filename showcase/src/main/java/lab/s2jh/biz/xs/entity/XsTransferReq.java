package lab.s2jh.biz.xs.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lab.s2jh.biz.xx.entity.XxJcxx;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.core.entity.annotation.SkipParamBind;
import lab.s2jh.core.entity.def.OperationAuditable;
import lab.s2jh.core.util.EnumUtils;
import lab.s2jh.core.web.json.JodaDateJsonSerializer;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "BIZ_XS_TRANSFER_REQ")
@MetaData(value = "学生异动")
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class XsTransferReq extends BaseEntity<String> implements OperationAuditable{

    @MetaData(value = "异动学生")
    @EntityAutoCode(order = 5)
    private XsJbxx xsJbxx;

    @MetaData(value = "异动新生成学生记录")
    @EntityAutoCode(order = 5)
    private XsJbxx newXsJbxx;

    @MetaData(value = "转出学校")
    @EntityAutoCode(order = 10)
    private XxJcxx sourceXx;

    @MetaData(value = "转入学校")
    @EntityAutoCode(order = 20)
    private XxJcxx targetXx;

    @MetaData(value = "申请时间")
    @EntityAutoCode(order = 40, edit = false)
    private Date reqTime;

    @MetaData(value = "异动说明")
    @EntityAutoCode(order = 45)
    private String reqExplain;

    private String reqUserId;

    @MetaData(value = "最后审批时间")
    @EntityAutoCode(order = 50, edit = false)
    private Date lastAuditTime;

    private String auditExplain;

    @MetaData(value = "状态")
    @EntityAutoCode(order = 60)
    private XsTransferReqStateEnum state = XsTransferReqStateEnum.S10DRAFT;

    private String id;

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        if (id == null || StringUtils.isBlank(id)) {
            this.id = null;
        } else {
            this.id = id;
        }
    }

    public static enum XsTransferReqStateEnum {
        @MetaData(value = "待提交")
        S10DRAFT,

        @MetaData(value = "转出学校提交待审")
        S20SBMTD,

        @MetaData(value = "转出学校上级审核通过")
        S30SRCPASSD,

        @MetaData(value = "转出学校上级审核未过")
        S40SRCNPASS,

        @MetaData(value = "转入学校上级审核通过")
        S50TGTPASSD,

        @MetaData(value = "转入学校上级审核未过")
        S60TGTNPASS,

        @MetaData(value = "转入学校已完成接收")
        S80TRANSED
    }

    public static enum XsTransferReqEventEnum {
        @MetaData(value = "提交")
        SUBMIT,

        @MetaData(value = "转出学校上级审核")
        SRC_AUDIT,

        @MetaData(value = "转入学校上级审核")
        TGT_AUDIT,

        @MetaData(value = "转入学校接收")
        TRANSFER;
    }

    @Override
    @Transient
    public String getDisplay() {
        return xsJbxx.getDisplay();
    }

    @OneToOne(optional = false)
    @JoinColumn(nullable = false, name = "xs_jbxx_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    public XsJbxx getXsJbxx() {
        return xsJbxx;
    }

    public void setXsJbxx(XsJbxx xsJbxx) {
        this.xsJbxx = xsJbxx;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_xx", referencedColumnName = "xxsbm", updatable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    public XxJcxx getSourceXx() {
        return sourceXx;
    }

    public void setSourceXx(XxJcxx sourceXx) {
        this.sourceXx = sourceXx;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "target_xx", referencedColumnName = "xxsbm")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    public XxJcxx getTargetXx() {
        return targetXx;
    }

    public void setTargetXx(XxJcxx targetXx) {
        this.targetXx = targetXx;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @JsonSerialize(using = JodaDateJsonSerializer.class)
    public Date getReqTime() {
        return reqTime;
    }

    @SkipParamBind
    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }

    @Column(length = 40, updatable = false, nullable = false)
    public String getReqUserId() {
        return reqUserId;
    }

    @SkipParamBind
    public void setReqUserId(String reqUserId) {
        this.reqUserId = reqUserId;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JodaDateJsonSerializer.class)
    public Date getLastAuditTime() {
        return lastAuditTime;
    }

    @SkipParamBind
    public void setLastAuditTime(Date lastAuditTime) {
        this.lastAuditTime = lastAuditTime;
    }

    @Column(nullable = false, length = 2000)
    public String getReqExplain() {
        return reqExplain;
    }

    public void setReqExplain(String reqExplain) {
        this.reqExplain = reqExplain;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    public XsTransferReqStateEnum getState() {
        return state;
    }

    @SkipParamBind
    public void setState(XsTransferReqStateEnum state) {
        this.state = state;
    }

    @OneToOne
    @JoinColumn(nullable = true, name = "new_xs_jbxx_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    public XsJbxx getNewXsJbxx() {
        return newXsJbxx;
    }

    public void setNewXsJbxx(XsJbxx newXsJbxx) {
        this.newXsJbxx = newXsJbxx;
    }

    @Column(length = 512)
    public String getAuditExplain() {
        return auditExplain;
    }

    public void setAuditExplain(String auditExplain) {
        this.auditExplain = auditExplain;
    }

    @Override
    public String convertStateToDisplay(String rawState) {
        return EnumUtils.getEnumDataMap(XsTransferReqStateEnum.class).get(rawState);
    }

    @Override
    public String convertEventToDisplay(String rawEvent) {
        return EnumUtils.getEnumDataMap(XsTransferReqEventEnum.class).get(rawEvent);
    }

}
