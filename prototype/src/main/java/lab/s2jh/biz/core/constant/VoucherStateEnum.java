package lab.s2jh.biz.core.constant;

import lab.s2jh.core.annotation.MetaData;

@MetaData("单据/凭证状态")
public enum VoucherStateEnum {
    @MetaData(value = "草稿")
    DRAFT,

    @MetaData(value = "提交")
    POST,

    @MetaData(value = "红冲")
    REDW;
}
