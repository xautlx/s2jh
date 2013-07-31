package lab.s2jh.biz.sys.service;

import java.util.List;

import lab.s2jh.biz.sys.dao.RegionCodeDao;
import lab.s2jh.biz.sys.entity.RegionCode;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.json.ValueLabelBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

@Service
@Transactional
public class RegionCodeService extends BaseService<RegionCode, String> {

    @Autowired
    private RegionCodeDao regionCodeDao;

    @Autowired
    private RegionCodeCacheService regionCodeCacheService;

    @Override
    protected BaseDao<RegionCode, String> getEntityDao() {
        return regionCodeDao;
    }

    public RegionCode findByRegionCode(String regionCode) {
        return regionCodeDao.findByRegionCode(regionCode);
    }

    /**
     * 返回Key-Value形式的省级行政区域数据
     * 
     * @return
     */
    @Transactional(readOnly = true)
    public List<ValueLabelBean> findProvinces() {
        List<ValueLabelBean> valueLabelBeans = Lists.newArrayList();
        List<ValueLabelBean> allValueLabelBeans = regionCodeCacheService.findAllCachedKeyValues();
        for (ValueLabelBean lvb : allValueLabelBeans) {
            if (lvb.getValue().endsWith("0000")) {
                valueLabelBeans.add(lvb);
            }
        }
        return valueLabelBeans;
    }

    /**
     * 返回Key-Value形式的省属地市行政区域数据
     * 
     * @return
     */
    @Transactional(readOnly = true)
    public List<ValueLabelBean> findCities(String regionCode) {
        Assert.isTrue(RegionCode.isProvice(regionCode));
        String provincePrefix = regionCode.substring(0, 2);
        List<ValueLabelBean> valueLabelBeans = Lists.newArrayList();
        List<ValueLabelBean> allValueLabelBeans = regionCodeCacheService.findAllCachedKeyValues();
        for (ValueLabelBean lvb : allValueLabelBeans) {
            String code = lvb.getValue();
            if (!code.endsWith("0000") && code.endsWith("00") && code.startsWith(provincePrefix)) {
                valueLabelBeans.add(lvb);
            }
        }
        return valueLabelBeans;
    }

    /**
     * 返回Key-Value形式的地市属区县行政区域数据
     * 
     * @return
     */
    @Transactional(readOnly = true)
    public List<ValueLabelBean> findDistricts(String regionCode) {
        Assert.isTrue(RegionCode.isCity(regionCode));
        String cityPrefix = regionCode.substring(0, 4);
        List<ValueLabelBean> valueLabelBeans = Lists.newArrayList();
        List<Object[]> keyValueInfos = regionCodeDao.findKeyValueInfo();
        for (Object[] objects : keyValueInfos) {
            String code = String.valueOf(objects[0]);
            if (!code.endsWith("00") && code.startsWith(cityPrefix)) {
                valueLabelBeans.add(new ValueLabelBean(code, String.valueOf(objects[1])));
            }
        }
        return valueLabelBeans;
    }

    /**
     * 计算当前行政区划层次路径集合
     * @param regionCode
     * @return
     */
    public String getRegionPaths(String regionCode) {
        List<ValueLabelBean> allRegions = regionCodeCacheService.findAllCachedKeyValues();
        for (ValueLabelBean region : allRegions) {
            if (region.getValue().equals(regionCode)) {
                return region.getLabel();
            }
        }
        return null;
    }
}
