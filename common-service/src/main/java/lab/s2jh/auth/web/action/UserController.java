package lab.s2jh.auth.web.action;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lab.s2jh.auth.entity.Privilege;
import lab.s2jh.auth.entity.Role;
import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.entity.UserR2Role;
import lab.s2jh.auth.service.PrivilegeService;
import lab.s2jh.auth.service.RoleService;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.service.R2OperationEnum;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.annotation.SecurityControllIgnore;
import lab.s2jh.core.web.json.ValueLabelBean;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.sys.service.MenuService;
import lab.s2jh.sys.vo.NavMenuVO;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class UserController extends BaseController<User, String> {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private MenuService menuService;

    @Autowired(required = false)
    private AclService aclService;

    @Override
    protected void checkEntityAclPermission(User entity) {
        if (aclService != null) {
            aclService.validateAuthUserAclCodePermission(entity.getAclCode());
        }
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
    protected BaseService<User, String> getEntityService() {
        return userService;
    }

    @MetaData(title = "计算显示角色关联数据")
    @SecurityControllIgnore
    public HttpHeaders findRelatedRoles() {
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildGroupFilterFromHttpRequest(Role.class, getRequest());
        List<Role> roles = roleService.findByFilters(groupFilter, new Sort(Direction.DESC, "aclType", "code"));
        List<UserR2Role> r2s = userService.findRelatedUserR2RolesForUser(this.getId());
        for (Role role : roles) {
            role.addExtraAttribute("related", false);
            for (UserR2Role r2 : r2s) {
                if (r2.getRole().equals(role)) {
                    role.addExtraAttribute("r2CreatedDate", r2.getCreatedDate());
                    role.addExtraAttribute("related", true);
                    break;
                }
            }
        }
        setModel(buildPageResultFromList(roles));
        return buildDefaultHttpHeaders();
    }

    @MetaData(title = "更新角色关联")
    public HttpHeaders doUpdateRelatedRoleR2s() {
        String userId = this.getId();
        Set<String> roleIds = this.getParameterIds("r2ids");
        R2OperationEnum op = Enum.valueOf(R2OperationEnum.class, getParameter("op", R2OperationEnum.add.name()));
        userService.updateRelatedRoleR2s(userId, roleIds, op);
        setModel(OperationResult.buildSuccessResult(op.getLabel() + "操作完成"));
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(title = "创建")
    public HttpHeaders doCreate() {
        /**
         * 判断选取的用户机构代码是否属于当前登录用户管辖范围
         * 该属性设定为不允许自动绑定，则需要手工从请求参数获取设置  @see lab.s2jh.auth.entity.User#setAclCode
         */
        String aclCode = this.getParameter("aclCode");
        bindingEntity.setAclCode(aclCode);
        userService.save(bindingEntity, this.getParameter("newpassword"));
        setModel(OperationResult.buildSuccessResult("创建操作成功", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(title = "更新")
    public HttpHeaders doUpdate() {
        String newpassword = this.getParameter("newpassword");
        if (StringUtils.isNotBlank(newpassword)) {
            userService.save(bindingEntity, newpassword);
        } else {
            userService.save(bindingEntity);
        }
        setModel(OperationResult.buildSuccessResult("更新操作成功", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(title = "查询")
    public HttpHeaders findByPage() {
        GroupPropertyFilter groupFilter = GroupPropertyFilter
                .buildGroupFilterFromHttpRequest(entityClass, getRequest());
        if (AuthContextHolder.getAuthUserDetails() != null) {
            Collection<String> aclCodePrefixs = AuthContextHolder.getAuthUserDetails().getAclCodePrefixs();
            if (!CollectionUtils.isEmpty(aclCodePrefixs)) {
                groupFilter.and(new PropertyFilter(MatchType.ACLPREFIXS, "aclCode", aclCodePrefixs));
            }
            Integer authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
            if (authUserAclType != null) {
                groupFilter.and(new PropertyFilter(MatchType.LE, "aclType", authUserAclType));
            }
        }        
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(getRequest());
        Page<User> page = this.getEntityService().findByPage(groupFilter, pageable);
        if (aclService != null) {
            Map<Integer, String> globalAclTypeMap = aclService.getAclTypeMap();
            for (User user : page.getContent()) {
                user.addExtraAttribute("aclTypeLabel", globalAclTypeMap.get(user.getAclType()));
            }
        }
        setModel(page);
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(title = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @MetaData(title = "机构选取的Autocomplete数据")
    @SecurityControllIgnore
    public HttpHeaders aclCodes() {
        List<ValueLabelBean> lvList = Lists.newArrayList();
        if (aclService != null) {
            String term = this.getParameter("term");
            if (term != null && term.length() >= 2) {
                Map<String, String> keyValueMap = aclService.findAclCodesMap();
                Collection<String> aclCodePrefixs = AuthContextHolder.getAuthUserDetails().getAclCodePrefixs();

                for (Map.Entry<String, String> me : keyValueMap.entrySet()) {
                    String key = me.getKey();
                    if (key.startsWith(term)) {
                        for (String aclCodePrefix : aclCodePrefixs) {
                            if (key.startsWith(aclCodePrefix)) {
                                lvList.add(new ValueLabelBean(me.getKey(), me.getValue()));
                            }
                        }
                    }
                }
            }
        }
        setModel(lvList);
        return buildDefaultHttpHeaders();
    }

    @MetaData(title = "汇总用户关联权限集合")
    public HttpHeaders privileges() {
        List<Privilege> privileges = userService.findRelatedPrivilegesForUser(this.getId());
        setModel(buildPageResultFromList(privileges));
        return buildDefaultHttpHeaders();
    }

    @MetaData(title = "汇总用户关联菜单集合")
    public HttpHeaders menus() {
        Set<GrantedAuthority> authsSet = new HashSet<GrantedAuthority>();
        List<Role> roles = roleService.findR2RolesForUser(this.getId());
        for (Role role : roles) {
            authsSet.add(new SimpleGrantedAuthority(role.getCode()));
        }
        List<NavMenuVO> menus = menuService.authUserMenu(authsSet, this.getRequest().getContextPath());
        setModel(menus);
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(title = "版本数据列表")
    public HttpHeaders revisionList() {
        return super.revisionList();
    }

    @Override
    @MetaData(title = "版本数据对比")
    public HttpHeaders revisionCompare() {
        return super.revisionCompare();
    }

}
