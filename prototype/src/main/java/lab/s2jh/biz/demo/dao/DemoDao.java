package lab.s2jh.biz.demo.dao;

import lab.s2jh.biz.demo.entity.Demo;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface DemoDao extends BaseDao<Demo, String> {

}