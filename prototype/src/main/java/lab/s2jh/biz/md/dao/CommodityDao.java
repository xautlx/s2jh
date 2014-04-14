package lab.s2jh.biz.md.dao;

import lab.s2jh.biz.md.entity.Commodity;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface CommodityDao extends BaseDao<Commodity, String> {

}