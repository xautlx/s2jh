package lab.s2jh.biz.sys.web.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.biz.sys.entity.RegionCode;
import lab.s2jh.biz.sys.service.RegionCodeService;
import lab.s2jh.biz.xx.service.XxJcxxService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.web.json.ValueLabelBean;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.RestActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/pub")
@MetaData(value = "机构代码")
public class AclCodeController extends RestActionSupport implements ModelDriven<Object> {

    @Autowired
    private RegionCodeService regionCodeService;

    @Autowired
    private XxJcxxService xxJcxxService;

    @Autowired
    private AclService aclService;
    
    private Object model;

    @Override
    public Object getModel() {
        return model;
    }

    /**
     * 查看对象显示页面
     * 
     * @return
     */
    public HttpHeaders label() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String value = request.getParameter("value");
        RegionCode regionCode = regionCodeService.findByRegionCode(value);
        if (regionCode != null) {
            model=regionCode.getDisplayLabel();
        }
        return new DefaultHttpHeaders().disableCaching();
    }

    public HttpHeaders autocomplete() {
        List<ValueLabelBean> lvList = Lists.newArrayList();
        HttpServletRequest request = ServletActionContext.getRequest();
        String term = request.getParameter("term");
        if (term != null && term.length() >= 2) {
            Map<String, String> keyValueMap = aclService.findAclCodesMap();
            for (Map.Entry<String, String> me : keyValueMap.entrySet()) {
                String key = me.getKey();
                if (key.startsWith(term)) {
                    lvList.add(new ValueLabelBean(me.getKey(), me.getValue()));
                }
            }
        }
        model=lvList;
        return new DefaultHttpHeaders().disableCaching();
    }
}