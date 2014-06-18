package lab.s2jh.profile.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.profile.entity.ProfileParamDef;
import lab.s2jh.profile.dao.ProfileParamDefDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileParamDefService extends BaseService<ProfileParamDef,String>{
    
    @Autowired
    private ProfileParamDefDao profileParamDefDao;

    @Override
    protected BaseDao<ProfileParamDef, String> getEntityDao() {
        return profileParamDefDao;
    }
}
