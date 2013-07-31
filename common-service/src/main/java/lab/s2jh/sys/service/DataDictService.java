package lab.s2jh.sys.service;

import java.util.List;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.sys.dao.DataDictDao;
import lab.s2jh.sys.entity.DataDict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataDictService extends BaseService<DataDict,String>{
    
    @Autowired
    private DataDictDao dataDictDao;

    @Override
    protected BaseDao<DataDict, String> getEntityDao() {
        return dataDictDao;
    }
    
    public List<String> findDistinctCategories(){
        return dataDictDao.findDistinctCategories();
    }
    
    public List<DataDict> findByCategory(String category){
        return dataDictDao.findByCategory(category);
    }
}
