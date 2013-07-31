package lab.s2jh.biz.xx.service;

import java.util.List;

import lab.s2jh.biz.core.service.RegionAclService;
import lab.s2jh.biz.xx.dao.XxBjDao;
import lab.s2jh.biz.xx.dao.XxJcxxDao;
import lab.s2jh.biz.xx.entity.XxJcxx;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.json.ValueLabelBean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional
public class XxJcxxService extends BaseService<XxJcxx, String> {

    @Autowired
    private XxJcxxDao xxJcxxDao;

    @Autowired
    private XxBjDao xxBjDao;

    @Autowired
    private AclService aclService;

    @Override
    protected BaseDao<XxJcxx, String> getEntityDao() {
        return xxJcxxDao;
    }

    /**
     * 查询行政区划所辖学校集合
     * @param regionCode
     * @return
     */
    @Transactional(readOnly = true)
    public List<XxJcxx> findMgtChildren(String xzqhm, String sszgdwm) {
        if (StringUtils.isBlank(sszgdwm) || RegionAclService.ZY_USER_ACL_CODE.equals(sszgdwm)) {
            return xxJcxxDao.findByXzqhmStartingWith(aclService.getAclCodePrefix(xzqhm));
        } else {
            return xxJcxxDao.findByXzqhmStartingWithAndSszgdwm(aclService.getAclCodePrefix(xzqhm), sszgdwm);
        }
    }

    @Transactional(readOnly = true)
    public XxJcxx findByXxdm(String xxdm) {
        return xxJcxxDao.findByXxdm(xxdm);
    }

    @Transactional(readOnly = true)
    public List<ValueLabelBean> findNjsOfXxdm(String xxdm) {
        List<ValueLabelBean> valueLabelBeans = Lists.newArrayList();
        List<Object[]> njs = xxBjDao.findNjsOfXxdm(xxdm);
        for (Object[] objects : njs) {
            ValueLabelBean vlb = new ValueLabelBean(String.valueOf(objects[0]), String.valueOf(objects[1]));
            valueLabelBeans.add(vlb);
        }
        return valueLabelBeans;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "XxJcxxSpringCache")
    public List<ValueLabelBean> findAllCachedKeyValues() {
        List<ValueLabelBean> valueLabelBeans = Lists.newArrayList();
        List<Object[]> keyValueInfos = xxJcxxDao.findKeyValueInfo();
        for (Object[] objects : keyValueInfos) {
            String code = String.valueOf(objects[0]);
            ValueLabelBean vlb = new ValueLabelBean(code, String.valueOf(objects[1]));
            valueLabelBeans.add(vlb);
        }
        return valueLabelBeans;
    }

    @Async
    @Transactional(readOnly = true)
    public void asyncInitCacheData() {
        Iterable<XxJcxx> xxJcxxs = xxJcxxDao.findAll();
        for (XxJcxx xxJcxx : xxJcxxs) {
            xxJcxxDao.findByXxdm(xxJcxx.getXxdm());
        }
    }
}
