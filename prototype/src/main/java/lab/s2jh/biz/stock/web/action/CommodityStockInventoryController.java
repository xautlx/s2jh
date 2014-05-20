package lab.s2jh.biz.stock.web.action;

import java.util.Map;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.web.SimpleController;
import lab.s2jh.core.web.view.OperationResult;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.struts2.rest.HttpHeaders;

import com.google.common.collect.Maps;

@MetaData("库存管理")
public class CommodityStockInventoryController extends SimpleController {

    public HttpHeaders edit() {
        return buildDefaultHttpHeaders("inputBasic");
    }

    public HttpHeaders findForInventory() {
        String barcode = getRequiredParameter("barcode");
        //模拟处理，返回实际实时商品库存数据
        Map<String, Object> mockData = Maps.newHashMap();
        mockData.put("curStockQuantity", RandomStringUtils.randomNumeric(3));

        Map<String, Object> mockCommodity = Maps.newHashMap();
        mockCommodity.put("id", RandomStringUtils.randomNumeric(3));
        mockCommodity.put("barcode", barcode);
        mockCommodity.put("display", "模拟商品" + RandomStringUtils.randomAlphabetic(5));

        mockData.put("commodity", mockCommodity);

        setModel(mockData);

        return buildDefaultHttpHeaders();
    }

    public HttpHeaders doCreate() {
        setModel(OperationResult.buildSuccessResult("盘存记录保存成功"));
        return buildDefaultHttpHeaders();
    }
}
