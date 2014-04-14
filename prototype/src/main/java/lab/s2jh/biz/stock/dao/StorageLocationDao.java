package lab.s2jh.biz.stock.dao;

import lab.s2jh.biz.stock.entity.StorageLocation;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface StorageLocationDao extends BaseDao<StorageLocation, String> {

}