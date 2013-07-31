package lab.s2jh.biz.sys.service;

import java.util.List;

import lab.s2jh.biz.sys.dao.RegionCodeDao;
import lab.s2jh.biz.sys.entity.RegionCode;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.json.ValueLabelBean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional
public class RegionCodeCacheService extends BaseService<RegionCode, String> {

    @Autowired
    private RegionCodeDao regionCodeDao;

    @Override
    protected BaseDao<RegionCode, String> getEntityDao() {
        return regionCodeDao;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "RegionCodeSpringCache")
    public List<ValueLabelBean> findAllCachedKeyValues() {
        List<ValueLabelBean> valueLabelBeans = Lists.newArrayList();
        List<Object[]> keyValueInfos = regionCodeDao.findKeyValueInfo();
        for (Object[] objects : keyValueInfos) {
            String code = String.valueOf(objects[0]);
            String label = String.valueOf(objects[1]);
            ValueLabelBean vlb = new ValueLabelBean(code, label);
            String parentCode = null;
            if (code.endsWith("0000")) {
                parentCode = null;
            } else if (code.endsWith("00")) {
                parentCode = code.substring(0, 2) + "0000";
            } else {
                parentCode = code.substring(0, 4) + "00";
            }
            if (StringUtils.isNotBlank(parentCode)) {
                for (ValueLabelBean item : valueLabelBeans) {
                    if (item.getValue().equals(parentCode)) {
                        vlb.setExtData(item.getLabel() + String.valueOf(objects[1]));
                        item.addChild(vlb);
                        break;
                    }
                }
            }
            valueLabelBeans.add(vlb);
        }
        return valueLabelBeans;
    }
}
