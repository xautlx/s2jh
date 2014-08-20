package lab.s2jh.auth.web.action;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import lab.s2jh.core.web.annotation.SecurityControlIgnore;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.web.action.BaseController;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@MetaData(value = "角色管理")
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

    @SecurityControlIgnore
    public HttpHeaders aclTypeMapData() {
        setModel(getAclTypeMap());
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(value = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    protected String isDisallowDelete(Role entity) {
        if (entity.getCode().equals(Role.ROLE_ADMIN_CODE) || entity.getCode().equals(Role.ROLE_ANONYMOUSLY_CODE)) {
            return "系统预置数据，不允许删除:" + entity.getDisplay();
        }
        return null;
    }

    @Override
    @MetaData(value = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData(value = "保存")
    public HttpHeaders doSave() {
        String authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
        if (StringUtils.isNotBlank(authUserAclType)) {
            //判断选取的类型是否属于当前登录用户管辖范围
            String aclType = this.getRequiredParameter("aclType");
            if (authUserAclType.compareTo(aclType) < 0) {
                throw new DataAccessDeniedException("数据访问权限不足");
            }
            bindingEntity.setAclType(aclType);
        }
        return super.doSave();
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

    @MetaData(value = "权限关联")
    @SecurityControlIgnore
    public HttpHeaders privileges() {
        Map<String, List<Privilege>> groupDatas = Maps.newLinkedHashMap();
        List<Privilege> privileges = privilegeService.findAllCached();
        List<RoleR2Privilege> r2s = roleService.findOne(this.getId()).getRoleR2Privileges();
        for (Privilege privilege : privileges) {
            List<Privilege> groupPrivileges = groupDatas.get(privilege.getCategory());
            if (groupPrivileges == null) {
                groupPrivileges = Lists.newArrayList();
                groupDatas.put(privilege.getCategory(), groupPrivileges);
            }
            groupPrivileges.add(privilege);
            privilege.addExtraAttribute("related", false);
            for (RoleR2Privilege r2 : r2s) {
                if (r2.getPrivilege().equals(privilege)) {
                    privilege.addExtraAttribute("r2CreatedDate", r2.getCreatedDate());
                    privilege.addExtraAttribute("related", true);
                    break;
                }
            }
        }
        this.getRequest().setAttribute("privileges", groupDatas);
        return buildDefaultHttpHeaders("privileges");
    }

    @MetaData(value = "更新权限关联")
    public HttpHeaders doUpdateRelatedPrivilegeR2s() {
        roleService.updateRelatedPrivilegeR2s(getId(), getParameterIds("r2ids"));
        setModel(OperationResult.buildSuccessResult("更新权限关联操作完成"));
        return buildDefaultHttpHeaders();
    }

    /**
     * 子类额外追加过滤限制条件的入口方法，一般基于当前登录用户强制追加过滤条件
     * 
     * @param filters
     */
    @Override
    protected void appendFilterProperty(GroupPropertyFilter groupPropertyFilter) {
        //限定查询ACL所辖范围数据
        String authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
        if (StringUtils.isNotBlank(authUserAclType)) {
            groupPropertyFilter.forceAnd(new PropertyFilter(MatchType.LE, "aclType", authUserAclType));
        }
    }

}