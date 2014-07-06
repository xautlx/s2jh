package lab.s2jh.biz.sale.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.auth.entity.Department;
import lab.s2jh.auth.entity.User;
import lab.s2jh.biz.core.constant.VoucherStateEnum;
import lab.s2jh.biz.core.entity.BaseBizEntity;
import lab.s2jh.biz.finance.entity.AccountSubject;
import lab.s2jh.biz.finance.entity.BizTradeUnit;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.annotation.SkipParamBind;
import lab.s2jh.core.web.json.DateJsonSerializer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@MetaData(value = "销售(发货)单")
@Entity
@Table(name = "biz_sale_delivery")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SaleDelivery extends BaseBizEntity {
    @MetaData("唯一凭证号")
    private String voucher;
    @MetaData("凭证日期")
    private Date voucherDate;
    @MetaData("凭证状态")
    private VoucherStateEnum voucherState = VoucherStateEnum.DRAFT;
    @MetaData("经办人")
    private User voucherUser;
    @MetaData("经办部门")
    private Department voucherDepartment;

    @MetaData(value = "参考来源", comments = "如填写京东，淘宝，天猫等标识销售来源的信息")
    private String referenceSource;
    @MetaData(value = "参考凭证号", comments = "如京东，淘宝，天猫等订单号等")
    private String referenceVoucher;

    @MetaData(value = "标题")
    private String title;
    @MetaData(value = "销售客户")
    private BizTradeUnit customerProfile;
    @MetaData(value = "销售备注")
    private String memo;

    @MetaData(value = "商品总成本", comments = "冗余汇总商品总成本，便于利润率统计")
    private BigDecimal commodityCostAmount;
    @MetaData(value = "折后商品总金额", comments = "冗余汇总商品销售金额，便于利润率统计")
    private BigDecimal commodityAmount;

    @MetaData(value = "收取客户运费")
    private BigDecimal chargeLogisticsAmount;
    @MetaData(value = "实际发货运费")
    private BigDecimal logisticsAmount;
    @MetaData(value = "发货快递公司", comments = "应付账款记账")
    private BizTradeUnit logistics;

    @MetaData(value = "指导售价总金额", comments = "未折扣之前原始指导售价总金额")
    private BigDecimal originalAmount;
    @MetaData(value = "商品优惠总金额", comments = "凡是影响商品毛利率统计的优惠金额，自动分摊到各商品优惠折扣金额中")
    private BigDecimal discountAmount;
    @MetaData(value = "其他优惠总金额", comments = "其他不影响商品毛利率的优惠金额，按照‘销售费用’科目记账")
    private BigDecimal otherDiscountAmount;
    @MetaData(value = "应收总金额", comments = "commodityAmount+chargeLogisticsAmount-otherDiscountAmount")
    private BigDecimal totalAmount;
    @MetaData(value = "已收款金额", comments = "剩余款项按照‘应收账款’记账")
    private BigDecimal payedAmount;

    @MetaData(value = "收款会计科目")
    private AccountSubject accountSubject;

    /** 快照记录发货地址信息  */
    private String deliveryAddr;
    private String mobilePhone;
    private String receivePerson;
    private String postCode;

    @MetaData(value = "毛利率")
    private BigDecimal profitRate;

    @MetaData(value = "毛利额")
    private BigDecimal profitAmount;

    @MetaData(value = "明细")
    private List<SaleDeliveryDetail> saleDeliveryDetails;

    @MetaData("提交时间")
    private Date submitDate;
    @MetaData("审核时间")
    private Date auditDate;
    @MetaData("红冲时间")
    private Date redwordDate;
    @MetaData(value = "最近操作摘要")
    private String lastOperationSummary;

    @Transient
    @Override
    public String getDisplay() {
        return voucher;
    }

    @Column(length = 128, nullable = false, unique = true, updatable = false)
    @JsonProperty
    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    @Column(length = 256)
    @JsonProperty
    public String getDeliveryAddr() {
        return deliveryAddr;
    }

    public void setDeliveryAddr(String deliveryAddr) {
        this.deliveryAddr = deliveryAddr;
    }

    @Column(length = 32)
    @JsonProperty
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Column(length = 16)
    @JsonProperty
    public String getReceivePerson() {
        return receivePerson;
    }

    public void setReceivePerson(String receivePerson) {
        this.receivePerson = receivePerson;
    }

    @Column(length = 16)
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
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

    @Column(length = 1000)
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @OneToMany(mappedBy = "saleDelivery", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<SaleDeliveryDetail> getSaleDeliveryDetails() {
        return saleDeliveryDetails;
    }

    public void setSaleDeliveryDetails(List<SaleDeliveryDetail> saleDeliveryDetails) {
        this.saleDeliveryDetails = saleDeliveryDetails;
    }

    @Column(precision = 18, scale = 2, nullable = false)
    @JsonProperty
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    @Column(precision = 18, scale = 2, nullable = false)
    public BigDecimal getCommodityAmount() {
        return commodityAmount;
    }

    public void setCommodityAmount(BigDecimal commodityAmount) {
        this.commodityAmount = commodityAmount;
    }

    @Column(precision = 18, scale = 2, nullable = true)
    public BigDecimal getCommodityCostAmount() {
        return commodityCostAmount;
    }

    public void setCommodityCostAmount(BigDecimal commodityCostAmount) {
        this.commodityCostAmount = commodityCostAmount;
    }

    @OneToOne
    @JoinColumn(name = "customer_profile_id", nullable = false)
    @JsonProperty
    public BizTradeUnit getCustomerProfile() {
        return customerProfile;
    }

    public void setCustomerProfile(BizTradeUnit customerProfile) {
        this.customerProfile = customerProfile;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 8, nullable = true)
    @JsonProperty
    public VoucherStateEnum getVoucherState() {
        return voucherState;
    }

    @SkipParamBind
    public void setVoucherState(VoucherStateEnum voucherState) {
        this.voucherState = voucherState;
    }

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getChargeLogisticsAmount() {
        return chargeLogisticsAmount;
    }

    public void setChargeLogisticsAmount(BigDecimal chargeLogisticsAmount) {
        this.chargeLogisticsAmount = chargeLogisticsAmount;
    }

    @Column(precision = 18, scale = 2, nullable = true)
    @JsonProperty
    public BigDecimal getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(BigDecimal payedAmount) {
        this.payedAmount = payedAmount;
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

    @JsonProperty
    public String getReferenceSource() {
        return referenceSource;
    }

    public void setReferenceSource(String referenceSource) {
        this.referenceSource = referenceSource;
    }

    @JsonProperty
    public String getReferenceVoucher() {
        return referenceVoucher;
    }

    public void setReferenceVoucher(String referenceVoucher) {
        this.referenceVoucher = referenceVoucher;
    }

    @Column(length = 2000)
    @JsonProperty
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToOne
    @JoinColumn(name = "account_subject_id")
    public AccountSubject getAccountSubject() {
        return accountSubject;
    }

    public void setAccountSubject(AccountSubject accountSubject) {
        this.accountSubject = accountSubject;
    }

    @Formula("(case when total_amount=0 then -1 else (total_amount - commodity_cost_amount)/total_amount end)")
    @JsonProperty
    @NotAudited
    public BigDecimal getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(BigDecimal profitRate) {
        this.profitRate = profitRate;
    }

    @Formula("(total_amount - commodity_cost_amount)")
    @JsonProperty
    @NotAudited
    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }

    public BigDecimal getLogisticsAmount() {
        return logisticsAmount;
    }

    @Column(precision = 18, scale = 2, nullable = true)
    public void setLogisticsAmount(BigDecimal logisticsAmount) {
        this.logisticsAmount = logisticsAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    @Column(precision = 18, scale = 2, nullable = true)
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Column(precision = 18, scale = 2, nullable = true)
    public BigDecimal getOtherDiscountAmount() {
        return otherDiscountAmount;
    }

    public void setOtherDiscountAmount(BigDecimal otherDiscountAmount) {
        this.otherDiscountAmount = otherDiscountAmount;
    }

    @Column(precision = 18, scale = 2, nullable = false)
    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public Date getRedwordDate() {
        return redwordDate;
    }

    public void setRedwordDate(Date redwordDate) {
        this.redwordDate = redwordDate;
    }

    public String getLastOperationSummary() {
        return lastOperationSummary;
    }

    public void setLastOperationSummary(String lastOperationSummary) {
        this.lastOperationSummary = lastOperationSummary;
    }
}
