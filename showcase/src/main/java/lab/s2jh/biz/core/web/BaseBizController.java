package lab.s2jh.biz.core.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lab.s2jh.biz.core.service.RegionAclService;
import lab.s2jh.biz.sys.service.EnumValueService;
import lab.s2jh.biz.sys.service.RegionCodeService;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.web.PersistableController;
import lab.s2jh.core.web.json.ValueLabelBean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

public abstract class BaseBizController<T extends PersistableEntity<ID>, ID extends Serializable> extends
        PersistableController<T, ID> {

    @Autowired
    private EnumValueService enumValueService;

    @Autowired
    private RegionCodeService regionCodeService;

    /**
     * 是否为用户显示行政区划导航
     * 
     * @return
     */
    public boolean isShowRegionNavForAuthUser() {
        String aclType = AuthContextHolder.getAuthUserDetails().getAclType();
        if (StringUtils.isNotBlank(aclType) && aclType.compareTo(RegionAclService.ACL_TYPE_DS) >= 0) {
            return true;
        }
        return false;
    }

    public Map<String, String> findEnumValuesByType(String enumType) {
        return enumValueService.findDisplayItemsByEnumType(enumType);
    }

    public Map<String, String> findProvinceRegions() {

        Map<String, String> dataMap = Maps.newLinkedHashMap();
        List<ValueLabelBean> valueLabelBeans = regionCodeService.findProvinces();
        for (ValueLabelBean valueLabelBean : valueLabelBeans) {
            dataMap.put(valueLabelBean.getValue(), valueLabelBean.getLabel());
        }
        return dataMap;
    }

    public Map<String, String> findCityRegions(String regionCode) {
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        if (StringUtils.isBlank(regionCode)) {
            return dataMap;
        }
        regionCode = StringUtils.substring(regionCode, 0, 2) + "0000";
        List<ValueLabelBean> valueLabelBeans = regionCodeService.findCities(regionCode);
        for (ValueLabelBean valueLabelBean : valueLabelBeans) {
            dataMap.put(valueLabelBean.getValue(), valueLabelBean.getLabel());
        }
        return dataMap;
    }

    public Map<String, String> findDistrictRegions(String regionCode) {
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        if (StringUtils.isBlank(regionCode)) {
            return dataMap;
        }
        regionCode = StringUtils.substring(regionCode, 0, 4) + "00";
        List<ValueLabelBean> valueLabelBeans = regionCodeService.findDistricts(regionCode);
        for (ValueLabelBean valueLabelBean : valueLabelBeans) {
            dataMap.put(valueLabelBean.getValue(), valueLabelBean.getLabel());
        }
        return dataMap;
    }

    public String findProvinceRegionCode(String regionCode) {
        if (regionCode != null) {
            return StringUtils.substring(regionCode, 0, 2) + "0000";
        }
        return regionCode;
    }

    public String findCityRegionCode(String regionCode) {
        if (regionCode != null) {
            return StringUtils.substring(regionCode, 0, 4) + "00";
        }
        return regionCode;
    }

    public String getRegionPaths(String regionCode) {
        return regionCodeService.getRegionPaths(regionCode);
    }
}
