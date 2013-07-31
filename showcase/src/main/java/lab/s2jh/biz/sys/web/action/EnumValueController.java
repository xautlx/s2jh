package lab.s2jh.biz.sys.web.action;

import java.util.Map;

import lab.s2jh.biz.core.web.BaseBizController;
import lab.s2jh.biz.sys.entity.EnumType.EnumTypes;
import lab.s2jh.biz.sys.entity.EnumValue;
import lab.s2jh.biz.sys.service.EnumValueService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.annotation.SecurityControllIgnore;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

public class EnumValueController extends BaseBizController<EnumValue, String> {

    @Autowired
    private EnumValueService enumValueService;

    @Override
    protected BaseService<EnumValue, String> getEntityService() {
        return enumValueService;
    }
    
    @Override
    protected void checkEntityAclPermission(EnumValue entity) {
        //Do nothing check
    }

    /** 缓存对象 */
    private static Map<String, Map<String, String>> lastCachedKeyValuesMap = Maps.newHashMap();

    @MetaData(title = "缓存集合数据")
    @SecurityControllIgnore
    public HttpHeaders data() {
        String type = this.getParameter("type");
        if (StringUtils.isNotBlank(type)) {
            Map<String, String> allCachedKeyValues = enumValueService.findDisplayItemsByEnumType(type);
            Map<String, String> lastCachedKeyValues = lastCachedKeyValuesMap.get(type);
            if (lastCachedKeyValues == null || lastCachedKeyValues != allCachedKeyValues) {
                lastCachedKeyValues = allCachedKeyValues;
                lastCachedKeyValuesMap.put(type, lastCachedKeyValues);
                setModel(lastCachedKeyValues);
                return buildDefaultHttpHeaders();
            } else {
                setModel(lastCachedKeyValues);
                return buildDefaultHttpHeaders();
            }
        } else {
            EnumTypes[] types = EnumTypes.values();
            for (EnumTypes item : types) {
                Map<String, String> allCachedKeyValues = enumValueService.findDisplayItemsByEnumType(item.name());
                lastCachedKeyValuesMap.put(item.name(), allCachedKeyValues);
            }
            setModel(lastCachedKeyValuesMap);
            return buildDefaultHttpHeaders();
        }

    }
   
}