package lab.s2jh.profile.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.profile.entity.ProfileParamVal;
import lab.s2jh.profile.dao.ProfileParamValDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileParamValService extends BaseService<ProfileParamVal,String>{
    
    @Autowired
    private ProfileParamValDao profileParamValDao;

    @Override
    protected BaseDao<ProfileParamVal, String> getEntityDao() {
        return profileParamValDao;
    }
}
