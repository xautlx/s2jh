package lab.s2jh.biz.xx.web.action;

import java.util.List;
import java.util.Map;

import lab.s2jh.biz.core.web.BaseBizController;
import lab.s2jh.biz.xx.entity.XxBj;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class XxBjController extends BaseBizController<XxBj, String> {

    @Autowired
    private XxBjService xxBjService;

    @Override
    protected BaseService<XxBj, String> getEntityService() {
        return xxBjService;
    }

    @Autowired
    private AclService aclService;

    @Override
    protected void checkEntityAclPermission(XxBj entity) {
        aclService.validateAuthUserAclCodePermission(entity.getXxdm());
    }

    @Override
    protected void appendFilterProperty(GroupPropertyFilter groupFilter) {
        super.appendFilterProperty(groupFilter);
        groupFilter.and(new PropertyFilter(MatchType.EQ, "xxdm", AuthContextHolder.getAclCodePrefix()));
    }
    
    @Override
    @MetaData(title = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    @MetaData(title = "自动提示完成数据")
    @SecurityControllIgnore
    public HttpHeaders autocomplete() {
        String val = this.getParameter(PARAM_NAME_FOR_AUTOCOMPLETE);
        List<Map<String, String>> datas = Lists.newArrayList();
        if (StringUtils.isNotBlank(val)) {
            String userAclCode = AuthContextHolder.getAuthUserDetails().getAclCode();
            List<XxBj> xxBjs = xxBjService.findByXxdmAndBhStartingWithOrBjmcLike(userAclCode, val);
            for (XxBj xxBj : xxBjs) {
                Map<String, String> row = Maps.newHashMap();
                row.put("id", xxBj.getId());
                row.put("bh", xxBj.getBh());
                row.put("bjmc", xxBj.getBjmc());
                row.put("value", xxBj.getBh());
                row.put("label", xxBj.getBjmc());
                datas.add(row);
            }
        }
        setModel(datas);
        return buildDefaultHttpHeaders();
    }

    @MetaData(title = "Remote校验班号是否合法")
    @SecurityControllIgnore
    public HttpHeaders validBh() {
        String bh = this.getParameter("bh");
        String userAclCode = AuthContextHolder.getAuthUserDetails().getAclCode();
        setModel(xxBjService.findByXxdmAndBh(userAclCode, bh) != null);
        return buildDefaultHttpHeaders();
    }
}