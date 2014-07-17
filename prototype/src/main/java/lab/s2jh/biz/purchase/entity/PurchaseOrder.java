package lab.s2jh.biz.purchase.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.auth.entity.Department;
import lab.s2jh.auth.entity.User;
import lab.s2jh.biz.core.entity.BaseBizEntity;
import lab.s2jh.biz.finance.entity.AccountSubject;
import lab.s2jh.biz.finance.entity.BizTradeUnit;
import lab.s2jh.bpm.BpmTrackable;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.annotation.SkipParamBind;
import lab.s2jh.core.web.json.DateJsonSerializer;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@MetaData("采购订单")
@Entity
@Table(name = "myt_purchase_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class PurchaseOrder extends BaseBizEntity implements BpmTrackable {

    private static final long serialVersionUID = -6347747732868248940L;
    
    @MetaData("唯一凭证号")
    private String voucher;
    @MetaData("凭证日期")
    private Date voucherDate;
    @MetaData("经办人")
    private User voucherUser;
    @MetaData("经办部门")
    private Department voucherDepartment;
    @MetaData("往来单位")
    private BizTradeUnit bizTradeUnit;

    private List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<PurchaseOrderDetail>();
    @MetaData(value = "标题摘要")
    private String title;
    @MetaData("采购备注")
    private String adminMemo;
    @MetaData("系统备注")
    private String sysMemo;
    @MetaData("订单状态")
    private PurchaseOrderStatusEnum orderStatus = PurchaseOrderStatusEnum.S10N;

    @MetaData("订单总金额")
    private BigDecimal totalAmount;

    @MetaData("已付总金额")
    private BigDecimal actualPayedAmount;

    @MetaData(value = "预付款会计科目")
    private AccountSubject accountSubject;
    @MetaData(value = "记账摘要")
    private String accountSummary;
    @MetaData("活动节点名称")
    private String activeTaskName;
    @MetaData("付款类型")
    private PurchaseOrderPayModeEnum payMode = PurchaseOrderPayModeEnum.PREV;
    @MetaData(value = "关联付款凭证号列表", comments = "逗号分隔，一般用于记录其他三方系统付款的凭证信息")
    private String paymentVouchers;
    @MetaData(value = "付款参考信息", comments = "一般用于记录其他三方系统付款的凭证信息")
    private String paymentReference;

    @MetaData("折后应付总金额")
    private BigDecimal amount;

    @MetaData("交税总金额")
    private BigDecimal totalTaxAmount;

    @MetaData("整单优惠金额")
    private BigDecimal totalDiscountAmount;

    @MetaData("整单运费")
    private BigDecimal totalDeliveryAmount;
    @MetaData("发货时间")
    private Date deliveryTime;

    @MetaData("快递公司")
    private BizTradeUnit logistics;

    @MetaData("快递单号")
    private String logisticsNo;

    @MetaData("提交时间")
    private Date submitDate;
    @MetaData("审核时间")
    private Date auditDate;
    @MetaData("红冲时间")
    private Date redwordDate;
    @MetaData(value = "最近操作摘要")
    private String lastOperationSummary;

    public enum PurchaseOrderStatusEnum {

        @MetaData("待提交")
        S10N,

        @MetaData("提交待审")
        S20S,

        @MetaData("审核通过")
        S30AP,

        @MetaData("审核未过")
        S40ANP,

        @MetaData("已下单")
        S50EX,

        @MetaData("部分收货")
        S60RECVS,

        @MetaData("完成收货")
        S65RECVE,

        @MetaData("完成付款")
        S75FIE,

        @MetaData("已关闭")
        S80C,

        @MetaData("已取消")
        S90CNC;

    }

    public enum PurchaseOrderPayModeEnum {

        @MetaData("预付")
        PREV,

        @MetaData("到付")
        POST;

    }

    @Column(length = 128, nullable = false, unique = true, updatable = false)
    @JsonProperty
    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    @Column(nullable = false)
    @JsonProperty
    @JsonSerialize(using = DateJsonSerializer.class)
    public Date getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "voucher_user_id", nullable = false)
    @JsonProperty
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    public User getVoucherUser() {
        return voucherUser;
    }

    public void setVoucherUser(User voucherUser) {
        this.voucherUser = voucherUser;
    }

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "voucher_department_id", nullable = false)
    @JsonProperty
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    public Department getVoucherDepartment() {
        return voucherDepartment;
    }

    public void setVoucherDepartment(Department voucherDepartment) {
        this.voucherDepartment = voucherDepartment;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    @JsonProperty
    public PurchaseOrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(PurchaseOrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<PurchaseOrderDetail> getPurchaseOrderDetails() {
        return purchaseOrderDetails;
    }

    public void setPurchaseOrderDetails(List<PurchaseOrderDetail> purchaseOrderDetails) {
        this.purchaseOrderDetails = purchaseOrderDetails;
    }

    @Column(length = 1024)
    @JsonProperty
    public String getAdminMemo() {
        return adminMemo;
    }

    public void setAdminMemo(String adminMemo) {
        this.adminMemo = adminMemo;
    }

    @Column(length = 128)
    public String getSysMemo() {
        return sysMemo;
    }

    public void setSysMemo(String sysMemo) {
        this.sysMemo = sysMemo;
    }

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "biz_trade_unit_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JsonProperty
    public BizTradeUnit getBizTradeUnit() {
        return bizTradeUnit;
    }

    public void setBizTradeUnit(BizTradeUnit bizTradeUnit) {
        this.bizTradeUnit = bizTradeUnit;
    }

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getActualPayedAmount() {
        return actualPayedAmount;
    }

    public void setActualPayedAmount(BigDecimal actualPayedAmount) {
        this.actualPayedAmount = actualPayedAmount;
    }

    @Override
    public String toString() {
        return "PurchaseOrder:  " + voucher;
    }

    @JsonProperty
    public String getActiveTaskName() {
        return activeTaskName;
    }

    public void setActiveTaskName(String activeTaskName) {
        this.activeTaskName = activeTaskName;
    }

    @JsonProperty
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 8, nullable = false)
    @JsonProperty
    public PurchaseOrderPayModeEnum getPayMode() {
        return payMode;
    }

    public void setPayMode(PurchaseOrderPayModeEnum payMode) {
        this.payMode = payMode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDeliveryAmount() {
        return totalDeliveryAmount;
    }

    public void setTotalDeliveryAmount(BigDecimal totalDeliveryAmount) {
        this.totalDeliveryAmount = totalDeliveryAmount;
    }

    @ManyToOne
    @JoinColumn(name = "account_subject_id")
    public AccountSubject getAccountSubject() {
        return accountSubject;
    }

    public void setAccountSubject(AccountSubject accountSubject) {
        this.accountSubject = accountSubject;
    }

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "logistics_id", nullable = true)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JsonProperty
    public BizTradeUnit getLogistics() {
        return logistics;
    }

    public void setLogistics(BizTradeUnit logistics) {
        this.logistics = logistics;
    }

    @Column(length = 32)
    @JsonProperty
    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    @JsonProperty
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @Override
    @Transient
    @JsonProperty
    public String getDisplay() {
        return voucher;
    }

    @Override
    @Transient
    @JsonIgnore
    public String getBpmBusinessKey() {
        return voucher;
    }

    public String getAccountSummary() {
        return accountSummary;
    }

    public void setAccountSummary(String accountSummary) {
        this.accountSummary = accountSummary;
    }

    public Date getRedwordDate() {
        return redwordDate;
    }

    @SkipParamBind
    public void setRedwordDate(Date redwordDate) {
        this.redwordDate = redwordDate;
    }

    @JsonProperty
    public String getLastOperationSummary() {
        return lastOperationSummary;
    }

    public void setLastOperationSummary(String lastOperationSummary) {
        this.lastOperationSummary = lastOperationSummary;
    }

    @Column(length = 2000)
    @JsonIgnore
    public String getPaymentVouchers() {
        return paymentVouchers;
    }

    public void setPaymentVouchers(String paymentVouchers) {
        this.paymentVouchers = paymentVouchers;
    }

    @Lob
    @JsonIgnore
    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    @SkipParamBind
    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    @JsonProperty
    public Date getSubmitDate() {
        return submitDate;
    }

    @SkipParamBind
    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    @Column(length = 2000)
    @JsonProperty
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}