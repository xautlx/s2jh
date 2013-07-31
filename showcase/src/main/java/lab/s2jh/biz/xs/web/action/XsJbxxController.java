package lab.s2jh.biz.xs.web.action;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.JoinType;

import lab.s2jh.biz.core.web.BaseBizController;
import lab.s2jh.biz.sys.entity.EnumType.EnumTypes;
import lab.s2jh.biz.sys.service.EnumValueService;
import lab.s2jh.biz.xs.entity.XsJbxx;
import lab.s2jh.biz.xs.service.XsJbxxService;
import lab.s2jh.biz.xx.service.XxBjService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.annotation.SecurityControllIgnore;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@MetaData(title = "学生信息管理")
public class XsJbxxController extends BaseBizController<XsJbxx, String> {

    @Autowired
    private XsJbxxService xsJbxxService;

    @Autowired
    private EnumValueService enumValueService;

    @Autowired
    private AclService aclService;

    @Autowired
    private XxBjService xxBjService;

    @Override
    protected BaseService<XsJbxx, String> getEntityService() {
        return xsJbxxService;
    }

    @Override
    protected void checkEntityAclPermission(XsJbxx entity) {
        aclService.validateAuthUserAclCodePermission(entity.getXxdm());
    }

    @Override
    @MetaData(title = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    @Override
    @MetaData(title = "更新")
    public HttpHeaders doUpdate() {
        String bh = this.getRequiredParameter("xxBjBh");
        bindingEntity.setXxBj(xxBjService.findByXxdmAndBh(bindingEntity.getXxdm(), bh));
        return super.doUpdate();
    }

    @Override
    @MetaData(title = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData(title = "创建")
    public HttpHeaders doCreate() {
        bindingEntity.setXxdm(AuthContextHolder.getAclCode());
        String bh = this.getRequiredParameter("xxBjBh");
        bindingEntity.setXxBj(xxBjService.findByXxdmAndBh(bindingEntity.getXxdm(), bh));
        return super.doCreate();
    }

    @MetaData(title = "自动提示完成数据")
    @SecurityControllIgnore
    public HttpHeaders autocomplete() {
        String val = this.getParameter(PARAM_NAME_FOR_AUTOCOMPLETE);
        List<Map<String, String>> datas = Lists.newArrayList();
        if (StringUtils.isNotBlank(val)) {
            List<XsJbxx> xsJbxxs = xsJbxxService.findByXhStartingWith(AuthContextHolder.getAclCode(), val);
            for (XsJbxx xsJbxx : xsJbxxs) {
                Map<String, String> row = Maps.newHashMap();
                row.put("id", xsJbxx.getId());
                row.put("xh", xsJbxx.getXh());
                row.put("xm", xsJbxx.getXm());
                row.put("xxdm", xsJbxx.getXxdm());
                datas.add(row);
            }
        }
        setModel(datas);
        return buildDefaultHttpHeaders();
    }

    @MetaData(title = "学号有效性验证")
    @SecurityControllIgnore
    public HttpHeaders validXh() {
        String aclCode = AuthContextHolder.getAclCode();
        if (StringUtils.isNotBlank(aclCode)) {
            String xh = this.getParameter("xh");
            setModel(xsJbxxService.findByXxdmAndXh(AuthContextHolder.getAclCode(), xh) != null);
        } else {
            setModel(true);
        }
        return buildDefaultHttpHeaders();
    }

    protected void exportXlsForGrid(GroupPropertyFilter groupFilter, Sort sort) {
        groupFilter.and(new PropertyFilter(MatchType.FETCH, "xsFzxx", JoinType.LEFT));
        groupFilter.and(new PropertyFilter(MatchType.FETCH, "xsKzxx", JoinType.LEFT));
        groupFilter.and(new PropertyFilter(MatchType.FETCH, "xsLxxx", JoinType.LEFT));
        List<XsJbxx> items = this.getEntityService().findByFilters(groupFilter, sort);
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("items", items);
        EnumTypes[] types = EnumTypes.values();
        for (EnumTypes type : types) {
            dataMap.put(type.name(), enumValueService.findDisplayItemsByEnumType(type.name()));
        }
        exportExcel("XS_XX_EXPORT.xls", "学生列表导出数据.xls", dataMap);
    }
}