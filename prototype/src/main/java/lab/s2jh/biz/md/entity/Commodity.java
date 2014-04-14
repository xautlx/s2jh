package lab.s2jh.biz.md.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.biz.stock.entity.StorageLocation;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseUuidEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "biz_commodity")
@MetaData(value = "商品")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class Commodity extends BaseUuidEntity {

    @MetaData(value = "唯一编码")
    private String sku;

    @MetaData(value = "实物条码")
    private String barcode;

    @MetaData(value = "商品名称")
    private String title;

    @MetaData(value = "成本价")
    private BigDecimal costPrice;

    @MetaData(value = "销售价")
    private BigDecimal salePrice;

    @MetaData(value = "默认库存地", description = "用于采购或销售时初始库存地")
    private StorageLocation defaultStorageLocation;

    @MetaData(value = "不可买", description = "显示商品信息，但处于不可购买状态")
    private Boolean soldOut = Boolean.FALSE;

    @MetaData(value = "已下架", description = "不显示商品信息只提示商品已下架")
    private Boolean removed = Boolean.FALSE;

    @Transient
    @JsonProperty
    public String getDisplay() {
        return sku + " " + title;
    }

    @Column(length = 64, unique = true, nullable = false)
    @JsonProperty
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @JsonProperty
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @JsonProperty
    @Column(length = 256, nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty
    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    @JsonProperty
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "Default_Storage_Location_ID")
    @NotAudited
    @JsonProperty
    public StorageLocation getDefaultStorageLocation() {
        return defaultStorageLocation;
    }

    public void setDefaultStorageLocation(StorageLocation defaultStorageLocation) {
        this.defaultStorageLocation = defaultStorageLocation;
    }

    @Column(nullable = false)
    public Boolean getSoldOut() {
        return soldOut;
    }

    public void setSoldOut(Boolean soldOut) {
        this.soldOut = soldOut;
    }

    @Column(nullable = false)
    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

}
