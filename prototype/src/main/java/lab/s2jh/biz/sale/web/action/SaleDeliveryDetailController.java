package lab.s2jh.biz.sale.web.action;

import lab.s2jh.biz.sale.entity.SaleDeliveryDetail;
import lab.s2jh.biz.sale.service.SaleDeliveryDetailService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.web.action.BaseController;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData("销售(发货)单明细管理")
public class SaleDeliveryDetailController extends BaseController<SaleDeliveryDetail,Long> {

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
}