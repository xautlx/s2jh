package lab.s2jh.biz.sys.web.action;

import java.util.List;
import java.util.Map;

import lab.s2jh.biz.core.service.RegionAclService;
import lab.s2jh.biz.core.web.BaseBizController;
import lab.s2jh.biz.sys.entity.RegionCode;
import lab.s2jh.biz.sys.service.RegionCodeCacheService;
import lab.s2jh.biz.sys.service.RegionCodeService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.annotation.SecurityControllIgnore;
import lab.s2jh.core.web.json.ValueLabelBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class RegionCodeController extends BaseBizController<RegionCode, String> {

    @Autowired
    private RegionCodeService regionCodeService;

    @Autowired
    private RegionCodeCacheService regionCodeCacheService;

    @Override
    protected BaseService<RegionCode, String> getEntityService() {
        return regionCodeService;
    }

    @Override
    protected void checkEntityAclPermission(RegionCode entity) {
        //Do nothing check
    }

    @Override
    @MetaData(title = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    @MetaData(title = "所有行政区域数据")
    @SecurityControllIgnore
    public HttpHeaders regionData() {
        List<ValueLabelBean> allCachedKeyValues = regionCodeCacheService.findAllCachedKeyValues();
        setModel(allCachedKeyValues);
        return new DefaultHttpHeaders().withETag(allCachedKeyValues);
    }

    @MetaData(title = "用户所辖行政区域")
    @SecurityControllIgnore
    public HttpHeaders regions() {
        String id = this.getParameter("id");
        boolean isParent = true;
        List<ValueLabelBean> regions = null;
        if (StringUtils.isBlank(id) || id.equals(RegionAclService.ZY_USER_ACL_CODE)) {
            regions = regionCodeService.findProvinces();
        } else if (RegionCode.isProvice(id)) {
            regions = regionCodeService.findCities(id);
        } else if (RegionCode.isCity(id)) {
            isParent = false;
            regions = regionCodeService.findDistricts(id);
        }
        List<Map<String, Object>> items = Lists.newArrayList();
        for (ValueLabelBean valueLabelBean : regions) {
            Map<String, Object> row = Maps.newHashMap();
            items.add(row);
            row.put("id", valueLabelBean.getValue());
            row.put("name", valueLabelBean.getLabel());
            row.put("isParent", isParent);
        }
        setModel(items);
        return buildDefaultHttpHeaders();
    }

    @SecurityControllIgnore
    public HttpHeaders cityRegions() {
        String val = this.getParameter("val");
        List<ValueLabelBean> datas = Lists.newArrayList();
        datas.add(new ValueLabelBean("", ""));
        if (StringUtils.isNotBlank(val)) {
            datas.addAll(regionCodeService.findCities(val));
        }
        setModel(datas);
        return buildDefaultHttpHeaders();
    }

    @SecurityControllIgnore
    public HttpHeaders districtRegions() {
        String val = this.getParameter("val");
        List<ValueLabelBean> datas = Lists.newArrayList();
        datas.add(new ValueLabelBean("", ""));
        if (StringUtils.isNotBlank(val)) {
            datas.addAll(regionCodeService.findDistricts(val));
        }
        setModel(datas);
        return buildDefaultHttpHeaders();
    }

    public void prepareView() {
        if (StringUtils.isBlank(getId())) {
            RegionCode persistentEntity = regionCodeService.findByRegionCode(this.getRequiredParameter("regionCode"));
            setModel(persistentEntity);
        } else {
            super.prepareView();
        }
    }
}