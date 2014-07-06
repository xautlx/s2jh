package lab.s2jh.biz.sale.web.action;

import lab.s2jh.biz.sale.entity.SaleDeliveryDetail;
import lab.s2jh.biz.sale.service.SaleDeliveryDetailService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.annotation.SecurityControllIgnore;
import lab.s2jh.web.action.BaseController;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData("销售(发货)单明细管理")
public class SaleDeliveryDetailController extends BaseController<SaleDeliveryDetail, Long> {

    @Autowired
    private SaleDeliveryDetailService saleDeliveryDetailService;

    @Override
    protected BaseService<SaleDeliveryDetail, Long> getEntityService() {
        return saleDeliveryDetailService;
    }

    @Override
    protected void checkEntityAclPermission(SaleDeliveryDetail entity) {
        // TODO Add acl check code logic
    }

    @Override
    @MetaData("查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    @MetaData(value = "销售商品毛利统计", comments = "由于可能出现完全赠品类型的0销售额订单，需要引入case when判断处理否则会出现除零错误")
    public HttpHeaders findByGroupCommodity() {
        setModel(findByGroupAggregate("commodity.id", "commodity.sku", "commodity.title",
                "max(case(equal(amount,0),-1,quot(diff(amount,costAmount),amount))) as maxProfitRate",
                "min(case(equal(amount,0),-1,quot(diff(amount,costAmount),amount))) as minProfitRate",
                "sum(diff(amount,costAmount)) as sumProfitAmount", "sum(amount)", "sum(quantity)",
                "case(equal(sum(amount),0),-1,quot(sum(diff(amount,costAmount)),sum(amount))) as avgProfitRate"));
        return buildDefaultHttpHeaders();
    }
}