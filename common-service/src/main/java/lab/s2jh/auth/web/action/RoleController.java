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
import org.apache.commons.lang3.StringUtils;
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

    public Map<String, String> getAclTypeMap() {
        Map<String, String> aclTypeMap = Maps.newLinkedHashMap();
        if (aclService != null) {
            String authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
            if (authUserAclType == null) {
                aclTypeMap = aclService.getAclTypeMap();
            } else {
                Map<String, String> globalAclTypeMap = aclService.getAclTypeMap();
                for (String aclType : globalAclTypeMap.keySet()) {
                    if (authUserAclType.compareTo(aclType) > 0) {
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
    @MetaData(value = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    @Override
    @MetaData(value = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData(value = "创建")
    public HttpHeaders doCreate() {
        String authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
        if (StringUtils.isNotBlank(authUserAclType)) {
            //判断选取的类型是否属于当前登录用户管辖范围
            String aclType = this.getRequiredParameter("aclType");
            if (authUserAclType.compareTo(aclType) < 0) {
                throw new DataAccessDeniedException("数据访问权限不足");
            }
            bindingEntity.setAclType(aclType);
        }
        return super.doCreate();
    }

    @Override
    @MetaData(value = "更新")
    public HttpHeaders doUpdate() {
        String authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
        if (StringUtils.isNotBlank(authUserAclType)) {
            //判断选取的类型是否属于当前登录用户管辖范围
            String aclType = this.getRequiredParameter("aclType");
            if (authUserAclType.compareTo(aclType) < 0) {
                throw new DataAccessDeniedException("数据访问权限不足");
            }
            bindingEntity.setAclType(aclType);
        }
        return super.doCreate();
    }

    @MetaData(value = "批量更新状态")
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

    @MetaData(value = "计算显示已经关联权限列表")
    @SecurityControllIgnore
    public HttpHeaders findRelatedRoleR2Privileges() {
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildGroupFilterFromHttpRequest(RoleR2Privilege.class,
                getRequest());
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(getRequest());
        setModel(roleService.findRelatedRoleR2PrivilegesForRole(this.getId(), groupFilter, pageable));
        return buildDefaultHttpHeaders();
    }

    @MetaData(value = "计算显示可选关联权限列表")
    @SecurityControllIgnore
    public HttpHeaders findUnRelatedPrivileges() {
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildGroupFilterFromHttpRequest(Privilege.class,
                getRequest());
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(getRequest());
        setModel(privilegeService.findUnRelatedPrivilegesForRole(this.getId(), groupFilter, pageable));
        return buildDefaultHttpHeaders();
    }

    @MetaData(value = "添加权限关联")
    public HttpHeaders doAddUnRelatedPrivilegeR2s() {
        String roleId = this.getId();
        Set<String> privilegeIds = this.getParameterIds("r2ids");
        roleService.addUnRelatedPrivilegeR2s(roleId, privilegeIds);
        setModel(OperationResult.buildSuccessResult("添加权限关联操作完成"));
        return buildDefaultHttpHeaders();
    }

    @MetaData(value = "移除权限关联")
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
        String authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
        if (StringUtils.isNotBlank(authUserAclType)) {
            filters.add(new PropertyFilter(MatchType.LE, "aclType", authUserAclType));
        }
    }

}