package lab.s2jh.biz.xs.dao;

import java.util.List;

import lab.s2jh.biz.xs.entity.XsXxImport;
import lab.s2jh.core.dao.BaseDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface XsXxImportDao extends BaseDao<XsXxImport, String> {

    @Modifying
    @Query("delete from XsXxImport where groupNum=?")
    void deleteByGroupNum(String groupNum);

    List<XsXxImport> findByGroupNumAndValidatePass(String groupNum, Boolean validatePass);
}