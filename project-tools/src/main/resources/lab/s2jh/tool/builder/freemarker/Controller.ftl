package ${root_package}.web.action;

import lab.s2jh.core.annotation.MetaData;
import ${root_package}.entity.${entity_name};
import ${root_package}.service.${entity_name}Service;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.view.OperationResult;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData("${model_title}管理")
public class ${entity_name}Controller extends BaseController<${entity_name},${id_type}> {

    @Autowired
    private ${entity_name}Service ${entity_name_uncapitalize}Service;

    @Override
    protected BaseService<${entity_name}, ${id_type}> getEntityService() {
        return ${entity_name_uncapitalize}Service;
    }
    
    @Override
    protected void checkEntityAclPermission(${entity_name} entity) {
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
}