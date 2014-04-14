package lab.s2jh.biz.stock.web.action;

import lab.s2jh.biz.stock.entity.StorageLocation;
import lab.s2jh.biz.stock.service.StorageLocationService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.view.OperationResult;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData("库存地管理")
public class StorageLocationController extends BaseController<StorageLocation,String> {

    @Autowired
    private StorageLocationService storageLocationService;

    @Override
    protected BaseService<StorageLocation, String> getEntityService() {
        return storageLocationService;
    }
    
    @Override
    protected void checkEntityAclPermission(StorageLocation entity) {
        // TODO Add acl check code logic
    }

    @MetaData("[TODO方法作用]")
    public HttpHeaders todo() {
        //TODO
        setModel(OperationResult.buildSuccessResult("TODO操作完成"));
        return buildDefaultHttpHeaders();
    }
    
    @Override
    @MetaData("创建")
    public HttpHeaders doCreate() {
        return super.doCreate();
    }

    @Override
    @MetaData("更新")
    public HttpHeaders doUpdate() {
        return super.doUpdate();
    }
    
    @Override
    @MetaData("保存")
    public HttpHeaders doSave() {
        return super.doSave();
    }

    @Override
    @MetaData("删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData("查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }
    
    @Override
    @MetaData(value = "下拉框选项数据")
    public HttpHeaders selectOptions() {
        return super.selectOptions();
    }
}