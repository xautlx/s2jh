package lab.s2jh.biz.stock.web.action;

import java.math.BigDecimal;

import lab.s2jh.biz.core.constant.VoucherTypeEnum;
import lab.s2jh.biz.md.entity.Commodity;
import lab.s2jh.biz.md.service.CommodityService;
import lab.s2jh.biz.stock.entity.CommodityStock;
import lab.s2jh.biz.stock.entity.StockInOut;
import lab.s2jh.biz.stock.entity.StorageLocation;
import lab.s2jh.biz.stock.service.CommodityStockService;
import lab.s2jh.biz.stock.service.StockInOutService;
import lab.s2jh.biz.stock.service.StorageLocationService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.service.Validation;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.web.action.BaseController;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData("商品库存")
public class CommodityStockController extends BaseController<CommodityStock, Long> {

    @Autowired
    private CommodityStockService commodityStockService;
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private StorageLocationService storageLocationService;
    @Autowired
    private StockInOutService stockInOutService;

    @Override
    protected BaseService<CommodityStock, Long> getEntityService() {
        return commodityStockService;
    }

    @Override
    protected void checkEntityAclPermission(CommodityStock entity) {
        // TODO Auto-generated method stub

    }

    @Override
    @MetaData("保存")
    public HttpHeaders doSave() {
        if (bindingEntity.isNotNew()) {
            CommodityStock oldCommodityStock = commodityStockService.findOne(bindingEntity.getId());
            if (!oldCommodityStock.getCurStockQuantity().equals(bindingEntity.getCurStockQuantity())
                    || !oldCommodityStock.getSalingTotalQuantity().equals(bindingEntity.getSalingTotalQuantity())
                    || !oldCommodityStock.getPurchasingTotalQuantity().equals(
                            bindingEntity.getPurchasingTotalQuantity())) {
                StockInOut stockInOut = new StockInOut();
                stockInOut.setCommodityStock(oldCommodityStock);
                stockInOut.setDiffQuantity(bindingEntity.getCurStockQuantity().subtract(
                        oldCommodityStock.getCurStockQuantity()));
                stockInOut.setDiffPurchasingQuantity(bindingEntity.getPurchasingTotalQuantity().subtract(
                        oldCommodityStock.getPurchasingTotalQuantity()));
                stockInOut.setDiffSalingQuantity(bindingEntity.getSalingTotalQuantity().subtract(
                        oldCommodityStock.getSalingTotalQuantity()));
                stockInOut.setOperationSummary("直接变更库存量数据");
                stockInOutService.saveCascade(stockInOut);
            } else {
                bindingEntity.setCurStockAmount(bindingEntity.getCostPrice().multiply(
                        bindingEntity.getCurStockQuantity()));
                getEntityService().save(bindingEntity);
            }
        } else {
            StockInOut stockInOut = new StockInOut();
            stockInOut.setCommodityStock(bindingEntity);
            stockInOut.setDiffQuantity(bindingEntity.getCurStockQuantity());
            stockInOut.setDiffPurchasingQuantity(bindingEntity.getPurchasingTotalQuantity());
            stockInOut.setDiffSalingQuantity(bindingEntity.getSalingTotalQuantity());
            bindingEntity.setCurStockQuantity(BigDecimal.ZERO);
            bindingEntity.setPurchasingTotalQuantity(BigDecimal.ZERO);
            bindingEntity.setSalingTotalQuantity(BigDecimal.ZERO);
            stockInOut.setOperationSummary("直接初始化库存量数据");
            stockInOutService.saveCascade(stockInOut);
        }
        setModel(OperationResult.buildSuccessResult("数据保存成功", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData("删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData("查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    @MetaData("盘存显示")
    public HttpHeaders inventory() {
        return buildDefaultHttpHeaders("inventory");
    }

    public HttpHeaders findForInventory() {
        String barcode = getRequiredParameter("barcode");
        Commodity commodity = commodityService.findByBarcode(barcode);
        if (commodity == null) {
            setModel(OperationResult.buildFailureResult("未知商品数据: " + barcode));
        } else {
            String batchNo = getParameter("batchNo");
            StorageLocation storageLocation = storageLocationService.findOne(getRequiredParameter("storageLocationId"));
            CommodityStock commodityStock = commodityStockService.findBy(commodity, storageLocation, batchNo);
            Validation.isTrue(commodityStock != null, "无库存数据，请检查录入数据或先初始化库存数据");
            setModel(commodityStock);
        }
        return buildDefaultHttpHeaders();
    }

    @MetaData("盘存")
    public HttpHeaders doInventory() {
        CommodityStock commodityStock = commodityStockService.findBy(bindingEntity.getCommodity(),
                bindingEntity.getStorageLocation(), bindingEntity.getBatchNo());
        StockInOut stockInOut = new StockInOut();
        stockInOut.setCommodityStock(commodityStock);
        stockInOut.setVoucherType(VoucherTypeEnum.PC);
        stockInOut.setDiffQuantity(bindingEntity.getCurStockQuantity().subtract(commodityStock.getCurStockQuantity()));
        String inventoryExplain = getParameter("inventoryExplain");
        if (StringUtils.isNotBlank(inventoryExplain)) {
            stockInOut.setOperationSummary("移动盘存:" + inventoryExplain);
        } else {
            stockInOut.setOperationSummary("移动盘存: 无变更登记");
        }
        stockInOutService.saveCascade(stockInOut);
        setModel(OperationResult.buildSuccessResult("数据保存成功", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    @MetaData(value = "按库存地汇总库存量")
    public HttpHeaders findByGroupStorageLocation() {
        setModel(findByGroupAggregate("commodity.id", "commodity.sku", "commodity.barcode", "commodity.title",
                "storageLocation.id", "sum(curStockQuantity)", "sum(salingTotalQuantity)",
                "sum(purchasingTotalQuantity)", "sum(stockThresholdQuantity)", "sum(availableQuantity) as sumAvailableQuantity"));
        return buildDefaultHttpHeaders();
    }

    @MetaData(value = "按商品汇总库存量")
    public HttpHeaders findByGroupCommodity() {
        setModel(findByGroupAggregate("commodity.id", "commodity.sku", "commodity.barcode", "commodity.title",
                "sum(curStockQuantity)", "sum(salingTotalQuantity)", "sum(purchasingTotalQuantity)",
                "sum(stockThresholdQuantity)", "sum(availableQuantity)"));
        return buildDefaultHttpHeaders();
    }
}