package lab.s2jh.biz.stock.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.biz.core.constant.VoucherTypeEnum;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseNativeEntity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;

@MetaData("库存变动明细记录")
@Entity
@Table(name = "biz_stock_in_out")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class StockInOut extends BaseNativeEntity {

    @MetaData("单据凭证号")
    private String voucher;
    @MetaData(value = "子凭证号")
    private String subVoucher;
    @MetaData("单据类型")
    private VoucherTypeEnum voucherType;

    @MetaData("商品库存信息")
    private CommodityStock commodityStock;
    @MetaData("原始数量")
    private BigDecimal originalQuantity = BigDecimal.ZERO;
    @MetaData("原始锁定量")
    private BigDecimal originalSalingQuantity = BigDecimal.ZERO;
    @MetaData("原始在途量")
    private BigDecimal originalPurchasingQuantity = BigDecimal.ZERO;

    @MetaData(value = "变更后数量", tooltips = "本次结余量(之前结余量-本次变更量)")
    private BigDecimal quantity = BigDecimal.ZERO;
    @MetaData(value = "变更后锁定量", tooltips = "本次结余量(之前结余量-本次变更量)")
    private BigDecimal salingQuantity = BigDecimal.ZERO;
    @MetaData(value = "变更后在途量", tooltips = "本次结余量(之前结余量-本次变更量)")
    private BigDecimal purchasingQuantity = BigDecimal.ZERO;

    @MetaData("数量变化")
    private BigDecimal diffQuantity = BigDecimal.ZERO;
    @MetaData("锁定量变化")
    private BigDecimal diffSalingQuantity = BigDecimal.ZERO;
    @MetaData("在途量变化")
    private BigDecimal diffPurchasingQuantity = BigDecimal.ZERO;

    @MetaData("原始成本价")
    private BigDecimal originalCostPrice = BigDecimal.ZERO;
    @MetaData("原始库存价值")
    private BigDecimal originalStockAmount = BigDecimal.ZERO;

    @MetaData("变更后成本价")
    private BigDecimal costPrice = BigDecimal.ZERO;
    @MetaData("变更后库存价值")
    private BigDecimal stockAmount = BigDecimal.ZERO;

    @MetaData(value = "操作摘要")
    private String operationSummary;
    @MetaData(value = "标识已红冲")
    private Boolean redword;

    public StockInOut() {
        super();
    }

    public StockInOut(String voucher, String subVoucher, VoucherTypeEnum voucherType, CommodityStock commodityStock) {
        BigDecimal zero = BigDecimal.ZERO;
        this.voucher = voucher;
        this.subVoucher = subVoucher;
        this.voucherType = voucherType;
        this.commodityStock = commodityStock;
        this.originalQuantity = commodityStock.getCurStockQuantity();
        this.originalSalingQuantity = commodityStock.getSalingTotalQuantity() == null ? zero : commodityStock
                .getSalingTotalQuantity();
        this.originalPurchasingQuantity = commodityStock.getPurchasingTotalQuantity() == null ? zero : commodityStock
                .getPurchasingTotalQuantity();
        this.originalCostPrice = commodityStock.getCostPrice();
        this.originalStockAmount = commodityStock.getCurStockAmount();
    }

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

    @Enumerated(EnumType.STRING)
    @Column(length = 8, nullable = true)
    @JsonProperty
    public VoucherTypeEnum getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(VoucherTypeEnum voucherType) {
        this.voucherType = voucherType;
    }

    @ManyToOne
    @JoinColumn(name = "commodity_stock_sid", nullable = false)
    @JsonProperty
    public CommodityStock getCommodityStock() {
        return commodityStock;
    }

    public void setCommodityStock(CommodityStock commodityStock) {
        this.commodityStock = commodityStock;
    }

    @JsonProperty
    public BigDecimal getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(BigDecimal originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    @JsonProperty
    public BigDecimal getOriginalSalingQuantity() {
        return originalSalingQuantity;
    }

    public void setOriginalSalingQuantity(BigDecimal originalSalingQuantity) {
        this.originalSalingQuantity = originalSalingQuantity;
    }

    @JsonProperty
    public BigDecimal getOriginalPurchasingQuantity() {
        return originalPurchasingQuantity;
    }

    public void setOriginalPurchasingQuantity(BigDecimal originalPurchasingQuantity) {
        this.originalPurchasingQuantity = originalPurchasingQuantity;
    }

    @JsonProperty
    public BigDecimal getOriginalCostPrice() {
        return originalCostPrice;
    }

    public void setOriginalCostPrice(BigDecimal originalCostPrice) {
        this.originalCostPrice = originalCostPrice;
    }

    @JsonProperty
    public BigDecimal getOriginalStockAmount() {
        return originalStockAmount;
    }

    public void setOriginalStockAmount(BigDecimal originalStockAmount) {
        this.originalStockAmount = originalStockAmount;
    }

    @JsonProperty
    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @JsonProperty
    public BigDecimal getSalingQuantity() {
        return salingQuantity;
    }

    public void setSalingQuantity(BigDecimal salingQuantity) {
        this.salingQuantity = salingQuantity;
    }

    @JsonProperty
    public BigDecimal getPurchasingQuantity() {
        return purchasingQuantity;
    }

    public void setPurchasingQuantity(BigDecimal purchasingQuantity) {
        this.purchasingQuantity = purchasingQuantity;
    }

    @JsonProperty
    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    @JsonProperty
    public BigDecimal getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(BigDecimal stockAmount) {
        this.stockAmount = stockAmount;
    }

    @JsonProperty
    public BigDecimal getDiffQuantity() {
        return diffQuantity;
    }

    public void setDiffQuantity(BigDecimal diffQuantity) {
        this.diffQuantity = diffQuantity;
    }

    @JsonProperty
    public BigDecimal getDiffSalingQuantity() {
        return diffSalingQuantity;
    }

    public void setDiffSalingQuantity(BigDecimal diffSalingQuantity) {
        this.diffSalingQuantity = diffSalingQuantity;
    }

    @JsonProperty
    public BigDecimal getDiffPurchasingQuantity() {
        return diffPurchasingQuantity;
    }

    public void setDiffPurchasingQuantity(BigDecimal diffPurchasingQuantity) {
        this.diffPurchasingQuantity = diffPurchasingQuantity;
    }

    @JsonProperty
    public String getOperationSummary() {
        return operationSummary;
    }

    public void setOperationSummary(String operationSummary) {
        this.operationSummary = operationSummary;
    }

    public void addOperationSummary(String operationSummary) {
        if (StringUtils.isBlank(this.operationSummary)) {
            this.operationSummary = operationSummary;
        } else {
            this.operationSummary += ";" + operationSummary;
        }
    }

    public Boolean getRedword() {
        return redword;
    }

    public void setRedword(Boolean redword) {
        this.redword = redword;
    }
}