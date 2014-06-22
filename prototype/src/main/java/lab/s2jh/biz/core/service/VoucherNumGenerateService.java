package lab.s2jh.biz.core.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lab.s2jh.biz.core.constant.VoucherTypeEnum;
import lab.s2jh.core.annotation.MetaData;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@MetaData(value = "凭证号生成服务", comments = "初期按照简化单实例处理模式，后期根据需要优化为集群模式")
public class VoucherNumGenerateService {

    private final static DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyMMddHHmmss");

    private final static DateFormat DEFAULT_FORMAT_YYMM = new SimpleDateFormat("yyMM");

    public String getVoucherNumByType(VoucherTypeEnum voucherType) {
        if (VoucherTypeEnum.XS.equals(voucherType)) {
            return DEFAULT_FORMAT_YYMM.format(new Date()) + RandomStringUtils.randomNumeric(6);
        }
        String num = DEFAULT_FORMAT.format(new Date()) + RandomStringUtils.randomNumeric(1);
        return voucherType.name() + num;
    }

    public static void main(String[] args) {
        System.out.println(new Date().getTime());
    }
}
