package lab.s2jh.biz.xx.web.action;

import java.util.List;
import java.util.Map;

import lab.s2jh.biz.core.web.BaseBizController;
import lab.s2jh.biz.xx.entity.XxJcxx;
import lab.s2jh.biz.xx.service.XxJcxxService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.annotation.SecurityControllIgnore;
import lab.s2jh.core.web.json.ValueLabelBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class XxJcxxController extends BaseBizController<XxJcxx, String> {

    @Autowired
    private XxJcxxService xxJcxxService;

    @Override
    protected BaseService<XxJcxx, String> getEntityService() {
        return xxJcxxService;
    }

    @Autowired
    private AclService aclService;

    @Override
    protected void checkEntityAclPermission(XxJcxx entity) {
        aclService.validateAuthUserAclCodePermission(entity.getXxdm());
    }

    @Override
    @MetaData(value = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    /** 缓存对象 */
    private static List<ValueLabelBean> lastCachedKeyValues;

    @MetaData(value = "所有学校集合数据")
    @SecurityControllIgnore
    public HttpHeaders data() {
        List<ValueLabelBean> allCachedKeyValues = xxJcxxService.findAllCachedKeyValues();
        if (lastCachedKeyValues == null || lastCachedKeyValues != allCachedKeyValues) {
            lastCachedKeyValues = allCachedKeyValues;
            setModel(lastCachedKeyValues);
            return buildDefaultHttpHeaders();
        } else {
            setModel(lastCachedKeyValues);
            return buildDefaultHttpHeaders();
        }
    }

    @MetaData(value = "自动提示完成数据")
    @SecurityControllIgnore
    public HttpHeaders autocomplete() {
        String val = this.getParameter(PARAM_NAME_FOR_AUTOCOMPLETE);
        List<Map<String, String>> datas = Lists.newArrayList();
        if (StringUtils.isNotBlank(val)) {
            List<ValueLabelBean> allCachedKeyValues = xxJcxxService.findAllCachedKeyValues();
            for (ValueLabelBean item : allCachedKeyValues) {
                if (item.getValue().startsWith(val) || item.getLabel().indexOf(val) > -1) {
                    Map<String, String> row = Maps.newHashMap();
                    row.put("xxdm", item.getValue());
                    row.put("xxmc", item.getLabel());
                    datas.add(row);
                }
            }
        }
        setModel(datas);
        return buildDefaultHttpHeaders();
    }

    @MetaData(value = "Remote校验学校代码是否合法")
    @SecurityControllIgnore
    public HttpHeaders validXxdm() {
        String xxdm = this.getParameter("xxdm");
        setModel(xxJcxxService.findByXxdm(xxdm) != null);
        return buildDefaultHttpHeaders();
    }

    public void prepareView() {
        if (StringUtils.isBlank(getId())) {
            XxJcxx persistentEntity = xxJcxxService.findByXxdm(this.getRequiredParameter("xxdm"));
            setModel(persistentEntity);
        } else {
            super.prepareView();
        }
    }
}