package lab.s2jh.sys.web.action;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.sys.entity.DataDict;
import lab.s2jh.sys.service.DataDictService;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData(value = "数据字典管理")
public class DataDictController extends BaseController<DataDict, String> {

    @Autowired
    private DataDictService dataDictService;

    @Override
    protected BaseService<DataDict, String> getEntityService() {
        return dataDictService;
    }

    @Override
    protected void checkEntityAclPermission(DataDict entity) {
        // Do nothing
    }

    @Override
    @MetaData(value = "保存")
    public HttpHeaders doSave() {
        return super.doSave();
    }

    @Override
    @MetaData(value = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    protected void appendFilterProperty(GroupPropertyFilter groupPropertyFilter) {
        if (groupPropertyFilter.isEmpty()) {
            groupPropertyFilter.forceAnd(new PropertyFilter(MatchType.NU, "parent.id", true));
        }
        super.appendFilterProperty(groupPropertyFilter);
    }

    @Override
    @MetaData(value = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }
}