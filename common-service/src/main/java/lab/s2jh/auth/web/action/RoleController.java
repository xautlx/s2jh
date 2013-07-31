package lab.s2jh.auth.web.action;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lab.s2jh.auth.entity.Privilege;
import lab.s2jh.auth.entity.Role;
import lab.s2jh.auth.entity.RoleR2Privilege;
import lab.s2jh.auth.service.PrivilegeService;
import lab.s2jh.auth.service.RoleService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.exception.DataAccessDeniedException;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.annotation.SecurityControllIgnore;
import lab.s2jh.core.web.view.OperationResult;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.google.common.collect.Maps;

public class RoleController extends BaseController<Role, String> {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired(required = false)
    private AclService aclService;

    @Override
    protected BaseService<Role, String> getEntityService() {
        return roleService;
    }

    @Override
    protected void checkEntityAclPermission(Role entity) {
        //Do nothing check
    }

    public Map<Integer, String> getAclTypeMap() {
        Map<Integer, String> aclTypeMap = Maps.newLinkedHashMap();
        if (aclService != null) {
            Integer authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
            if (authUserAclType == null) {
                aclTypeMap = aclService.getAclTypeMap();
            } else {
                Map<Integer, String> globalAclTypeMap = aclService.getAclTypeMap();
                for (Integer aclType : globalAclTypeMap.keySet()) {
                    if (authUserAclType >= aclType) {
                        aclTypeMap.put(aclType, globalAclTypeMap.get(aclType));
                    }
                }
            }
        }
        return aclTypeMap;
    }

    @SecurityControllIgnore
    public HttpHeaders aclTypeMapData() {
        setModel(getAclTypeMap());
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(title = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    @Override
    @MetaData(title = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData(title = "创建")
    public HttpHeaders doCreate() {
        Integer authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
        if (authUserAclType != null && authUserAclType > 0) {
            //判断选取的类型是否属于当前登录用户管辖范围
            Integer aclType = Integer.valueOf(this.getRequiredParameter("aclType"));
            if (authUserAclType < aclType) {
                throw new DataAccessDeniedException("数据访问权限不足");
            }
            bindingEntity.setAclType(aclType);
        }
        return super.doCreate();
    }

    @Override
    @MetaData(title = "更新")
    public HttpHeaders doUpdate() {
        Integer authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
        if (authUserAclType != null) {
            //判断选取的类型是否属于当前登录用户管辖范围
            Integer aclType = Integer.valueOf(this.getRequiredParameter("aclType"));
            if (authUserAclType < aclType) {
                throw new DataAccessDeniedException("数据访问权限不足");
            }
            bindingEntity.setAclType(aclType);
        }
        return super.doCreate();
    }

    @MetaData(title = "批量更新状态")
    public HttpHeaders doState() {
        boolean disabled = BooleanUtils.toBoolean(this.getRequiredParameter("disabled"));
        Collection<Role> entities = this.getEntitiesByParameterIds();
        for (Role entity : entities) {
            entity.setDisabled(disabled);
        }
        getEntityService().save(entities);
        setModel(OperationResult.buildSuccessResult("批量更新状态操作成功"));
        return buildDefaultHttpHeaders();
    }

    @MetaData(title = "计算显示已经关联权限列表")
    @SecurityControllIgnore
    public HttpHeaders findRelatedRoleR2Privileges() {
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildGroupFilterFromHttpRequest(RoleR2Privilege.class,
                getRequest());
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(getRequest());
        setModel(roleService.findRelatedRoleR2PrivilegesForRole(this.getId(), groupFilter, pageable));
        return buildDefaultHttpHeaders();
    }

    @MetaData(title = "计算显示可选关联权限列表")
    @SecurityControllIgnore
    public HttpHeaders findUnRelatedPrivileges() {
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildGroupFilterFromHttpRequest(Privilege.class,
                getRequest());
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(getRequest());
        setModel(privilegeService.findUnRelatedPrivilegesForRole(this.getId(), groupFilter, pageable));
        return buildDefaultHttpHeaders();
    }

    @MetaData(title = "添加权限关联")
    public HttpHeaders doAddUnRelatedPrivilegeR2s() {
        String roleId = this.getId();
        Set<String> privilegeIds = this.getParameterIds("r2ids");
        roleService.addUnRelatedPrivilegeR2s(roleId, privilegeIds);
        setModel(OperationResult.buildSuccessResult("添加权限关联操作完成"));
        return buildDefaultHttpHeaders();
    }

    @MetaData(title = "移除权限关联")
    public HttpHeaders doDeleteRelatedPrivilegeR2s() {
        String roleId = this.getId();
        Set<String> r2Ids = this.getParameterIds("r2ids");
        roleService.deleteRelatedPrivilegeR2s(roleId, r2Ids);
        setModel(OperationResult.buildSuccessResult("移除权限关联操作完成"));
        return buildDefaultHttpHeaders();
    }

    /**
     * 子类额外追加过滤限制条件的入口方法，一般基于当前登录用户强制追加过滤条件
     * 
     * @param filters
     */
    protected void appendFilterProperty(List<PropertyFilter> filters) {
        //限定查询ACL所辖范围数据
        Integer authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
        if (authUserAclType != null) {
            filters.add(new PropertyFilter(MatchType.LE, "aclType", authUserAclType));
        }
    }

}