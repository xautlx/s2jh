package lab.s2jh.biz.xs.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lab.s2jh.biz.xs.dao.XsXxImportDao;
import lab.s2jh.biz.xs.entity.XsXxImport;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class XsXxImportService extends BaseService<XsXxImport, String> {

    private final static int IMPORT_BATCH_FLUSH_SIZE = 100;

    @Autowired
    private XsXxImportDao xsXxImportDao;

    @Autowired
    private XsJbxxService xsJbxxService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected BaseDao<XsXxImport, String> getEntityDao() {
        return xsXxImportDao;
    }

    public void uploadImportData(String groupNum, List<XsXxImport> xsXxImports) {
        //先清除已有数据
        xsXxImportDao.deleteByGroupNum(groupNum);
        //批量写入导入数据存储表
        int cnt = 0;
        for (XsXxImport xsXxImport : xsXxImports) {
            xsXxImportDao.save(xsXxImport);
            if (cnt % IMPORT_BATCH_FLUSH_SIZE == 0) {
                entityManager.flush();
            }
        }
    }

    public List<XsXxImport> findByGroupNumAndValidatePass(String groupNum) {
        return xsXxImportDao.findByGroupNumAndValidatePass(groupNum, true);
    }
}
