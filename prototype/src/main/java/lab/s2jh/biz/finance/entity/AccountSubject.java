package lab.s2jh.biz.finance.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.biz.core.entity.BaseBizEntity;
import lab.s2jh.core.annotation.MetaData;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@MetaData("会计科目")
@Entity
@Table(name = "biz_finance_account_subject")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class AccountSubject extends BaseBizEntity implements Comparable<AccountSubject> {

    @MetaData(value = "科目代码", tooltips = "请按照层级规范设置，创建之后不可修改")
    private String code;

    private AccountSubject parent;

    @MetaData(value = "科目名称")
    private String name;

    @MetaData(value = "备注说明")
    private String memo;

    //以下为会计结算和账期处理等业务冗余记录数据
    /**本期发生金额*/
    private BigDecimal amount;
    /**期初金额*/
    private BigDecimal amount00;
    /**第01期发生金额*/
    private BigDecimal amount01;
    /**第02期发生金额*/
    private BigDecimal amount02;
    private BigDecimal amount03;
    private BigDecimal amount04;
    private BigDecimal amount05;
    private BigDecimal amount06;
    private BigDecimal amount07;
    private BigDecimal amount08;
    private BigDecimal amount09;
    private BigDecimal amount10;
    private BigDecimal amount11;
    private BigDecimal amount12;

    @MetaData(value = "借贷余额方向标识", comments = "用于简化收支/利润等汇总统计")
    private Boolean balanceDirection;

    @Column(nullable = false, length = 25, updatable = false, unique = true)
    @JsonProperty
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    public AccountSubject getParent() {
        return parent;
    }

    public void setParent(AccountSubject parent) {
        this.parent = parent;
    }

    @Column(nullable = false, length = 64)
    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 1024)
    @JsonProperty
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @JsonProperty
    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount00() {
        return amount00;
    }

    public void setAmount00(BigDecimal amount00) {
        this.amount00 = amount00;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount01() {
        return amount01;
    }

    public void setAmount01(BigDecimal amount01) {
        this.amount01 = amount01;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount02() {
        return amount02;
    }

    public void setAmount02(BigDecimal amount02) {
        this.amount02 = amount02;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount03() {
        return amount03;
    }

    public void setAmount03(BigDecimal amount03) {
        this.amount03 = amount03;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount04() {
        return amount04;
    }

    public void setAmount04(BigDecimal amount04) {
        this.amount04 = amount04;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount05() {
        return amount05;
    }

    public void setAmount05(BigDecimal amount05) {
        this.amount05 = amount05;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount06() {
        return amount06;
    }

    public void setAmount06(BigDecimal amount06) {
        this.amount06 = amount06;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount07() {
        return amount07;
    }

    public void setAmount07(BigDecimal amount07) {
        this.amount07 = amount07;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount08() {
        return amount08;
    }

    public void setAmount08(BigDecimal amount08) {
        this.amount08 = amount08;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount09() {
        return amount09;
    }

    public void setAmount09(BigDecimal amount09) {
        this.amount09 = amount09;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount10() {
        return amount10;
    }

    public void setAmount10(BigDecimal amount10) {
        this.amount10 = amount10;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount11() {
        return amount11;
    }

    public void setAmount11(BigDecimal amount11) {
        this.amount11 = amount11;
    }

    @Column(precision = 18, scale = 2)
    public BigDecimal getAmount12() {
        return amount12;
    }

    public void setAmount12(BigDecimal amount12) {
        this.amount12 = amount12;
    }

    @Override
    public int compareTo(AccountSubject o) {
        return CompareToBuilder.reflectionCompare(this.getCode(), o.getCode());
    }

    @Override
    @JsonProperty
    @Transient
    public String getDisplay() {
        return code + " " + name;
    }

    public Boolean getBalanceDirection() {
        return balanceDirection;
    }

    public void setBalanceDirection(Boolean balanceDirection) {
        this.balanceDirection = balanceDirection;
    }
}