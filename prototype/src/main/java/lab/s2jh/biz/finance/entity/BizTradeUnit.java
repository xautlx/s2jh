package lab.s2jh.biz.finance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.biz.core.entity.BaseBizEntity;
import lab.s2jh.core.annotation.MetaData;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;

@MetaData("业务往来单位")
@Entity
@Table(name = "biz_trade_unit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BizTradeUnit extends BaseBizEntity {

    @MetaData(value = "代码")
    private String code;

    @MetaData(value = "名称")
    private String name;

    @MetaData(value = "单位类型")
    private BizTradeUnitTypeEnum type;

    @MetaData(value = "地址")
    private String addr;

    @MetaData(value = "办公电话")
    private String officePhone;

    @MetaData(value = "移动电话")
    private String mobilePhone;

    public static enum BizTradeUnitTypeEnum {

        @MetaData(value = "客户")
        CUSTOMER,

        @MetaData(value = "供应商")
        SUPPLIER;

    }

    @Override
    @Transient
    @JsonProperty
    public String getDisplay() {
        return code + " " + name;
    }

    @Column(nullable = false, length = 25, unique = true)
    @JsonProperty
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 128, nullable = false)
    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    @JsonProperty
    public BizTradeUnitTypeEnum getType() {
        return type;
    }

    public void setType(BizTradeUnitTypeEnum type) {
        this.type = type;
    }

    @Column(length = 1000)
    @JsonProperty
    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Column(length = 128)
    @JsonProperty
    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    @Column(length = 128)
    @JsonProperty
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
