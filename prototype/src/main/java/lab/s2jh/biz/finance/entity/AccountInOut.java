package lab.s2jh.biz.finance.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lab.s2jh.biz.core.constant.VoucherTypeEnum;
import lab.s2jh.biz.core.entity.BaseBizEntity;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.web.json.DateJsonSerializer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@MetaData("会计分录明细账")
@Entity
@Table(name = "myt_finance_account_in_out")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class AccountInOut extends BaseBizEntity {

    @MetaData("凭证号")
    private String voucher;
    @MetaData(value = "子凭证号", comments = "如销售单行项号")
    private String subVoucher;
    @MetaData("来源凭证类型")
    private VoucherTypeEnum voucherType;
    @MetaData("关联会计科目")
    private AccountSubject accountSubject;
    @MetaData(value = "会计科目代码", comments = "冗余属性，简化业务层记账调用accountSubject对象")
    private String accountSubjectCode;
    @MetaData(value = "借贷记账方向", comments = "true=借，false=贷")
    private Boolean accountDirection;
    @MetaData(value = "记账摘要")
    private String accountSummary;
    @MetaData("往来单位")
    private BizTradeUnit bizTradeUnit;
    @MetaData("金额")
    private BigDecimal amount;

    @MetaData("记录日期")
    private Date documentDate = new Date();

    @MetaData("记账日期 ")
    private Date postingDate = new Date();

    @MetaData("备注")
    private String adminMemo;

    @MetaData("会计分录明细账")
    private BigDecimal directionAmount;

    @MetaData(value = "标识记账已红冲")
    private Boolean redword;

    @Column(length = 128, nullable = false)
    @JsonProperty
    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    @Column(length = 128, nullable = true)
    @JsonProperty
    public String getSubVoucher() {
        return subVoucher;
    }

    public void setSubVoucher(String subVoucher) {
        this.subVoucher = subVoucher;
    }

    @Column(precision = 18, scale = 2, nullable = false)
    @JsonProperty
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    @JsonProperty
    public VoucherTypeEnum getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(VoucherTypeEnum voucherType) {
        this.voucherType = voucherType;
    }

    @Column(nullable = false)
    @JsonProperty
    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    @Column(nullable = false)
    @JsonProperty
    @JsonSerialize(using = DateJsonSerializer.class)
    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    @Lob
    public String getAdminMemo() {
        return adminMemo;
    }

    public void setAdminMemo(String adminMemo) {
        this.adminMemo = adminMemo;
    }

    @ManyToOne
    @JoinColumn(name = "account_subject_id", nullable = false)
    @JsonProperty
    public AccountSubject getAccountSubject() {
        return accountSubject;
    }

    public void setAccountSubject(AccountSubject accountSubject) {
        this.accountSubject = accountSubject;
    }

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "biz_trade_unit_id", nullable = true)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JsonProperty
    @ForeignKey(name = "none")
    public BizTradeUnit getBizTradeUnit() {
        return bizTradeUnit;
    }

    public void setBizTradeUnit(BizTradeUnit bizTradeUnit) {
        this.bizTradeUnit = bizTradeUnit;
    }

    @Column(length = 32, nullable = false)
    public String getAccountSubjectCode() {
        return accountSubjectCode;
    }

    public void setAccountSubjectCode(String accountSubjectCode) {
        this.accountSubjectCode = accountSubjectCode;
    }

    @JsonProperty
    public Boolean getAccountDirection() {
        return accountDirection;
    }

    public void setAccountDirection(Boolean accountDirection) {
        this.accountDirection = accountDirection;
    }

    @Column(length = 1000)
    @JsonProperty
    public String getAccountSummary() {
        return accountSummary;
    }

    public void setAccountSummary(String accountSummary) {
        this.accountSummary = accountSummary;
    }

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getDirectionAmount() {
        return directionAmount;
    }

    public void setDirectionAmount(BigDecimal directionAmount) {
        this.directionAmount = directionAmount;
    }

    public Boolean getRedword() {
        return redword;
    }

    public void setRedword(Boolean redword) {
        this.redword = redword;
    }
}