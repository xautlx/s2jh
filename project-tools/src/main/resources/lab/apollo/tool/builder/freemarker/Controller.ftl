package ${root_package}.web.action;

import lab.s2jh.core.annotation.MetaData;
import ${root_package}.entity.${entity_name};
import ${root_package}.service.${entity_name}Service;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.view.OperationResult;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData(title = "[TODO控制器名称]")
public class ${entity_name}Controller extends BaseController<${entity_name},String> {

    @Autowired
    private ${entity_name}Service ${entity_name_uncapitalize}Service;

    @Override
    protected BaseService<${entity_name}, String> getEntityService() {
        return ${entity_name_uncapitalize}Service;
    }
    
    @Override
    protected void checkEntityAclPermission(${entity_name} entity) {
        // TODO Add acl check code logic
    }

    @MetaData(title = "[TODO方法作用]")
    public HttpHeaders todo() {
        //TODO
        setModel(OperationResult.buildSuccessResult("TODO操作完成"));
        return buildDefaultHttpHeaders();
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
}