package lab.s2jh.biz.purchase.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

@MetaData("采购订单行项")
@Entity
@Table(name = "myt_purchase_order_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class PurchaseOrderDetail extends BaseBizEntity {
    @MetaData(value = "子凭证号")
    private String subVoucher;
    @MetaData("采购订单")
    private PurchaseOrder purchaseOrder;
    @MetaData("商品")
    private Commodity commodity;
    @MetaData("库存地")
    private StorageLocation storageLocation;

    @MetaData("采购量")
    private BigDecimal quantity;

    @MetaData("采购价格")
    private BigDecimal price;

    @MetaData("订单已收货量")
    private BigDecimal recvQuantity = BigDecimal.ZERO;
    @MetaData("单位")
    private String measureUnit;

    @MetaData("原价金额")
    private BigDecimal originalAmount;

    @MetaData("折扣率(%)")
    private BigDecimal discountRate;

    @MetaData("折扣额")
    private BigDecimal discountAmount;

    @MetaData("折后金额")
    private BigDecimal amount;
    /**税率(%) */
    /*private BigDecimal taxRate;
    *//** 税额 */
    /*
    private BigDecimal taxAmount;
    *//** 价税总金额 */
    /*
    private BigDecimal commodityAndTaxAmount;*/

    @MetaData("分摊运费 ")
    private BigDecimal deliveryAmount;

    @MetaData("入库成本价")
    private BigDecimal costPrice;

    @MetaData("采购商品成本金额")
    private BigDecimal costAmount;

    @Column(length = 128, nullable = true)
    @JsonProperty
    public String getSubVoucher() {
        return subVoucher;
    }

    public void setSubVoucher(String subVoucher) {
        this.subVoucher = subVoucher;
    }

    @ManyToOne
    @JoinColumn(name = "ORDER_SID", nullable = false)
    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "COMMODITY_SID", nullable = false)
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
    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getRecvQuantity() {
        return recvQuantity;
    }

    public void setRecvQuantity(BigDecimal recvQuantity) {
        this.recvQuantity = recvQuantity;
    }

    @Column(precision = 18, scale = 2, nullable = false)
    @JsonProperty
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /*@Column(precision = 18, scale = 2)
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

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getCommodityAndTaxAmount() {
        return commodityAndTaxAmount;
    }

    public void setCommodityAndTaxAmount(BigDecimal commodityAndTaxAmount) {
        this.commodityAndTaxAmount = commodityAndTaxAmount;
    }
    */
    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(BigDecimal deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    @Column(precision = 18, scale = 2)
    @JsonProperty
    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

}