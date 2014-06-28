package lab.s2jh.biz.sale.service;

import java.math.BigDecimal;

import lab.s2jh.biz.md.dao.CommodityDao;
import lab.s2jh.biz.md.entity.Commodity;
import lab.s2jh.biz.sale.dao.SaleDeliveryDao;
import lab.s2jh.biz.sale.entity.SaleDelivery;
import lab.s2jh.biz.sale.entity.SaleDeliveryDetail;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.service.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SaleDeliveryService extends BaseService<SaleDelivery, Long> {

    @Autowired
    private SaleDeliveryDao saleDeliveryDao;

    @Autowired
    private CommodityDao commodityDao;

    @Override
    protected BaseDao<SaleDelivery, Long> getEntityDao() {
        return saleDeliveryDao;
    }

    @Override
    public SaleDelivery save(SaleDelivery entity) {
        BigDecimal commodityCostAmount = BigDecimal.ZERO;
        BigDecimal originalAmount = BigDecimal.ZERO;
        BigDecimal commodityAmount = BigDecimal.ZERO;
        for (SaleDeliveryDetail sdd : entity.getSaleDeliveryDetails()) {
            sdd.setSaleDelivery(entity);
            Commodity commodity = commodityDao.findOne(sdd.getCommodity().getId());
            //先按照最简单的商品上面维护成本价，后面完善为分库存地成本价维护
            Validation.notNull(commodity.getCostPrice(), "请首先维护商品[" + commodity.getSku() + "]成本价");
            sdd.setCostPrice(commodity.getCostPrice());
            sdd.setCostAmount(sdd.getCostPrice().multiply(sdd.getQuantity()));

            commodityCostAmount = commodityCostAmount.add(sdd.getCostAmount());
            originalAmount = originalAmount.add(sdd.getOriginalAmount());
            commodityAmount = commodityAmount.add(sdd.getAmount());
        }

        entity.setCommodityCostAmount(commodityCostAmount);
        entity.setOriginalAmount(originalAmount);
        entity.setCommodityAmount(commodityAmount);
        return super.save(entity);
    }
}
