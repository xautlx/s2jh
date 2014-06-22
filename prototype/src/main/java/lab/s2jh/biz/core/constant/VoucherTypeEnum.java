package lab.s2jh.biz.core.constant;

import lab.s2jh.core.annotation.MetaData;

@MetaData("单据/凭证类型")
public enum VoucherTypeEnum {

    @MetaData(value = "采购订单")
    JHD,

    @MetaData(value = "采购(入库)单")
    JH,

    @MetaData(value = "销售订单")
    XSD,

    @MetaData(value = "销售单")
    XS,

    @MetaData(value = "调拨单")
    DH,

    @MetaData(value = "付款单")
    FKD,

    @MetaData(value = "盘存")
    PC,

    @MetaData(value = "退货")
    TH,

    @MetaData(value = "收款单")
    SKD;
}
