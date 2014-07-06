package lab.s2jh.biz.stock.dao;

import java.util.List;

import lab.s2jh.biz.md.entity.Commodity;
import lab.s2jh.biz.stock.entity.CommodityStock;
import lab.s2jh.biz.stock.entity.StorageLocation;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityStockDao extends BaseDao<CommodityStock, Long> {

    @Query("from CommodityStock where commodity=:commodity and storageLocation=:storageLocation and (batchNo is null or batchNo='')")
    CommodityStock findByCommodityAndStorageLocation(@Param("commodity") Commodity commodity,
            @Param("storageLocation") StorageLocation storageLocation);

    CommodityStock findByCommodityAndStorageLocationAndBatchNo(Commodity commodity, StorageLocation storageLocation,
            String batchNo);

    List<CommodityStock> findByCommodity(Commodity commodity);
}