package lab.s2jh.sys.web.action;

import java.util.List;
import java.util.Map;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.sys.entity.DataDict;
import lab.s2jh.sys.service.DataDictService;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@MetaData(title = "数据字典管理")
public class DataDictController extends BaseController<DataDict, String> {

    @Autowired
    private DataDictService dataDictService;

    @Override
    protected BaseService<DataDict, String> getEntityService() {
        return dataDictService;
    }

    @Override
    protected void checkEntityAclPermission(DataDict entity) {
        // TODO Add acl check code logic
    }

    @Override
    @MetaData(title = "创建")
    public HttpHeaders doCreate() {
        return super.doCreate();
    }

    @Override
    @MetaData(title = "更新")
    public HttpHeaders doUpdate() {
        return super.doUpdate();
    }

    @Override
    @MetaData(title = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData(title = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    public Map<String, String> getCategoryMap() {
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        for (String category : dataDictService.findDistinctCategories()) {
            dataMap.put(category, category);
        }
        return dataMap;
    }

    private List<DataDict> batchDataDicts = Lists.newArrayList();

    public void setBatchDataDicts(List<DataDict> batchDataDicts) {
        this.batchDataDicts = batchDataDicts;
    }
    
    public List<DataDict> getBatchDataDicts() {
        return batchDataDicts;
    }

    @MetaData(title = "批量添加")
    public HttpHeaders doCreateBatch() {
        dataDictService.save(batchDataDicts);
        setModel(OperationResult.buildSuccessResult("批量添加完成"));
        return buildDefaultHttpHeaders();
    }


}