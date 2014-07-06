package lab.s2jh.biz.stock.web.action;

import lab.s2jh.biz.stock.entity.StockInOut;
import lab.s2jh.biz.stock.service.StockInOutService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.web.action.BaseController;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData("StockInOutController")
public class StockInOutController extends BaseController<StockInOut, Long> {

    @Autowired
    private StockInOutService stockInOutService;

    @Override
    protected BaseService<StockInOut, Long> getEntityService() {
        return stockInOutService;
    }

    @Override
    protected void checkEntityAclPermission(StockInOut entity) {

    }

    @Override
    @MetaData("查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

}