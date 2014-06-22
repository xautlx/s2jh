package lab.s2jh.biz.sale.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.biz.core.entity.BaseBizEntity;
import lab.s2jh.biz.md.entity.Commodity;
import lab.s2jh.biz.stock.entity.StorageLocation;
import lab.s2jh.core.annotation.MetaData;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonProperty;

@MetaData(value = "销售(发货)单明细")
@Entity
@Table(name = "biz_sale_delivery_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class SaleDeliveryDetail extends BaseBizEntity {
    private SaleDelivery saleDelivery;
    private Commodity commodity;
    private BigDecimal price;
    private BigDecimal quantity;

    /** 单位 */
    private String measureUnit;
    /** 原价金额 */
    private BigDecimal originalAmount;
    /**折扣率(%) */
    private BigDecimal discountRate;
    /** 折扣额 */
    private BigDecimal discountAmount;
    /**税率(%) */
    private BigDecimal taxRate;
    /** 税额 */
    private BigDecimal taxAmount;
    /**折后金额 */
    private BigDecimal amount;
    private BigDecimal costPrice;
    private BigDecimal costAmount;
    private StorageLocation storageLocation;
    private Boolean gift = Boolean.FALSE;
    /** 含税总金额 */
    private BigDecimal commodityAndTaxAmount;

    @ManyToOne
    @JoinColumn(name = "sale_delivery_id", nullable = false)
    @JsonProperty
    public SaleDelivery getSaleDelivery() {
        return saleDelivery;
    }

    public void setSaleDelivery(SaleDelivery saleDelivery) {
        this.saleDelivery = saleDelivery;
    }

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "commodity_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JsonProperty
    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    @Column(precision = 18, scale = 2, nullable = false)
    @JsonProperty
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(precision = 18, scale = 2, nullable = false)
    @JsonProperty
    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "storage_location_sid")
    @NotAudited
    @JsonProperty
    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    @JsonProperty
    @Column(name = "is_gift")
    public Boolean getGift() {
        return gift;
    }

    public void setGift(Boolean gift) {
        this.gift = gift;
    }

    @Column(precision = 18, scale = 2, nullable = false)
    @JsonProperty
    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @Column(precision = 18, scale = 2, nullable = false)
    @JsonProperty
    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    @Column(precision = 18, scale = 2, nullable = true)
    @JsonProperty
    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    @Column(precision = 18, scale = 2, nullable = true)
    @JsonProperty
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Column(precision = 18, scale = 2, nullable = false)
    @JsonProperty
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    @Column(precision = 18, scale = 2, nullable = false)
    @JsonProperty
    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    @JsonProperty
    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    @Column(precision = 18, scale = 2, nullable = false)
    @JsonProperty
    public BigDecimal getCommodityAndTaxAmount() {
        return commodityAndTaxAmount;
    }

    public void setCommodityAndTaxAmount(BigDecimal commodityAndTaxAmount) {
        this.commodityAndTaxAmount = commodityAndTaxAmount;
    }

    @Override
    @Transient
    public String getDisplay() {
        return saleDelivery.getDisplay() + " " + commodity.getDisplay();
    }
}