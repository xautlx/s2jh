package lab.s2jh.biz.xs.web.action;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import lab.s2jh.biz.core.service.RegionAclService;
import lab.s2jh.biz.core.web.BaseBizController;
import lab.s2jh.biz.sys.service.EnumValueService;
import lab.s2jh.biz.xs.entity.XsJbxx;
import lab.s2jh.biz.xs.imp.validation.EnumTypeValue;
import lab.s2jh.biz.xs.service.XsJbxxService;
import lab.s2jh.biz.xx.entity.XxBj;
import lab.s2jh.biz.xx.entity.XxJcxx;
import lab.s2jh.biz.xx.service.XxBjService;
import lab.s2jh.biz.xx.service.XxJcxxService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.security.AuthUserDetails;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.annotation.SecurityControllIgnore;
import lab.s2jh.core.web.json.ValueLabelBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@MetaData(title = "学籍管理")
public class XsXxMgtController extends BaseBizController<XsJbxx, String> {

    @Autowired
    private XsJbxxService xsJbxxService;

    @Autowired
    private XxJcxxService xxJcxxService;

    @Autowired
    private XxBjService xxBjService;

    @Autowired
    private AclService aclService;

    @Autowired
    private EnumValueService enumValueService;

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

    public AuthUserDetails getAuthUserDetails() {
        return AuthContextHolder.getAuthUserDetails();
    }

    @MetaData(title = "用户所辖行政区域")
    @SecurityControllIgnore
    public HttpHeaders xxNjBjNav() {
        List<Map<String, Object>> items = Lists.newArrayList();
        String type = this.getParameter("type");
        String regionCode = this.getParameter("regionCode");
        String authUserAclCode = AuthContextHolder.getAclCode();
        if (StringUtils.isBlank(type)) {
            Integer authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
            if (RegionAclService.ACL_TYPE_XX.equals(authUserAclType)) {
                List<ValueLabelBean> valueLabelBeans = xxJcxxService.findNjsOfXxdm(authUserAclCode);
                for (ValueLabelBean vlb : valueLabelBeans) {
                    Map<String, Object> row = Maps.newHashMap();
                    row.put("id", vlb.getValue());
                    row.put("name", vlb.getLabel());
                    row.put("isParent", true);
                    row.put("type", "nj");
                    row.put("nj", vlb.getValue());
                    row.put("xxdm", authUserAclCode);
                    items.add(row);
                }
            } else {
                List<XxJcxx> xxJcxxs = xxJcxxService.findMgtChildren(regionCode, authUserAclCode);
                for (XxJcxx xxJcxx : xxJcxxs) {
                    Map<String, Object> row = Maps.newHashMap();
                    row.put("id", xxJcxx.getId());
                    row.put("name", xxJcxx.getXxmc());
                    row.put("isParent", true);
                    row.put("type", "xx");
                    row.put("xxdm", xxJcxx.getXxdm());
                    row.put("nj", "");
                    items.add(row);
                }
            }
        } else {
            if ("xx".equals(type)) {
                String xxdm = this.getRequiredParameter("xxdm");
                List<ValueLabelBean> valueLabelBeans = xxJcxxService.findNjsOfXxdm(xxdm);
                for (ValueLabelBean vlb : valueLabelBeans) {
                    Map<String, Object> row = Maps.newHashMap();
                    row.put("id", vlb.getValue());
                    row.put("name", vlb.getLabel());
                    row.put("isParent", true);
                    row.put("type", "nj");
                    row.put("nj", vlb.getValue());
                    row.put("xxdm", xxdm);
                    items.add(row);
                }
            } else if ("nj".equals(type)) {
                String xxdm = this.getRequiredParameter("xxdm");
                String nj = this.getRequiredParameter("nj");
                List<XxBj> xxBjs = xxBjService.findByXxdm(xxdm, nj);
                for (XxBj xxBj : xxBjs) {
                    Map<String, Object> row = Maps.newHashMap();
                    row.put("id", xxBj.getId());
                    row.put("name", xxBj.getBjmc());
                    row.put("isParent", false);
                    row.put("type", "bj");
                    row.put("nj", nj);
                    row.put("xxdm", xxdm);
                    items.add(row);
                }
            }
        }
        setModel(items);
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(title = "版本数据列表")
    public HttpHeaders revisionList() {
        return super.revisionList();
    }

    @Override
    @MetaData(title = "版本数据对比")
    public HttpHeaders revisionCompare() {
        return super.revisionCompare();
    }

    protected String convertPropertyDisplay(Object entity, Field field, Object value) {
        if (value == null) {
            return "";
        }
        EnumTypeValue enumTypeValue = field.getAnnotation(EnumTypeValue.class);
        if (enumTypeValue != null) {
            return enumValueService.findDisplayItemsByEnumType(enumTypeValue.value().name()).get(String.valueOf(value));
        }
        return super.convertPropertyDisplay(entity, field, value);
    }
}