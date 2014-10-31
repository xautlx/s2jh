package lab.s2jh.auth.web.action;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lab.s2jh.auth.entity.Department;
import lab.s2jh.auth.entity.Privilege;
import lab.s2jh.auth.entity.Role;
import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.entity.UserR2Role;
import lab.s2jh.auth.service.DepartmentService;
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
import lab.s2jh.core.service.Validation;
import lab.s2jh.core.web.annotation.SecurityControlIgnore;
import lab.s2jh.core.web.json.ValueLabelBean;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.sys.service.MenuService;
import lab.s2jh.sys.vo.NavMenuVO;
import lab.s2jh.web.action.BaseController;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class UserController extends BaseController<User, Long> {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private DepartmentService departmentService;

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

    public Map<String, String> getAclTypeMap() {
        Map<String, String> aclTypeMap = Maps.newLinkedHashMap();
        if (aclService != null) {
            String authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
            if (StringUtils.isBlank(authUserAclType)) {
                aclTypeMap = aclService.getAclTypeMap();
            } else {
                Map<String, String> globalAclTypeMap = aclService.getAclTypeMap();
                for (String aclType : globalAclTypeMap.keySet()) {
                    if (authUserAclType.compareTo(aclType) >= 0) {
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
    protected BaseService<User, Long> getEntityService() {
        return userService;
    }

    @MetaData(value = "角色关联")
    @SecurityControlIgnore
    public HttpHeaders roles() {
        List<Role> roles = Lists.newArrayList();
        List<Role> allRoles = roleService.findAllCached();
        List<UserR2Role> r2s = userService.findOne(this.getId()).getUserR2Roles();
        for (Role role : allRoles) {
            if (Role.ROLE_ANONYMOUSLY_CODE.equals(role.getCode())) {
                continue;
            }
            role.addExtraAttribute("related", false);
            for (UserR2Role r2 : r2s) {
                if (r2.getRole().equals(role)) {
                    role.addExtraAttribute("r2CreatedDate", r2.getCreatedDate());
                    role.addExtraAttribute("related", true);
                    break;
                }
            }
            roles.add(role);
        }
        this.getRequest().setAttribute("roles", roles);
        return buildDefaultHttpHeaders("roles");
    }

    @MetaData(value = "更新角色关联")
    public HttpHeaders doUpdateRelatedRoleR2s() {
        if (bindingEntity.getSigninid().equals("admin")) {
            Validation.notDemoMode();
        }
        userService.updateRelatedRoleR2s(getId(), getParameterIds("r2ids"));
        setModel(OperationResult.buildSuccessResult("更新角色关联操作完成"));
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(value = "保存")
    public HttpHeaders doSave() {
        if (bindingEntity.getSigninid().equals("admin")) {
            Validation.notDemoMode();
        }
        /**
         * 判断选取的用户机构代码是否属于当前登录用户管辖范围
         * 该属性设定为不允许自动绑定，则需要手工从请求参数获取设置  @see lab.s2jh.auth.entity.User#setAclCode
         */
        String aclCode = this.getParameter("aclCode");
        bindingEntity.setAclCode(aclCode);
        if (bindingEntity.isNew()) {
            userService.save(bindingEntity, this.getRequiredParameter("newpassword"));
            setModel(OperationResult.buildSuccessResult("创建操作成功", bindingEntity));
        } else {
            String newpassword = this.getParameter("newpassword");
            if (StringUtils.isNotBlank(newpassword)) {
                userService.save(bindingEntity, newpassword);
            } else {
                userService.save(bindingEntity);
            }
            setModel(OperationResult.buildSuccessResult("更新操作成功", bindingEntity));
        }
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(value = "查询")
    public HttpHeaders findByPage() {
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildFromHttpRequest(entityClass, getRequest());
        if (AuthContextHolder.getAuthUserDetails() != null) {
            Collection<String> aclCodePrefixs = AuthContextHolder.getAuthUserDetails().getAclCodePrefixs();
            if (!CollectionUtils.isEmpty(aclCodePrefixs)) {
                groupFilter.forceAnd(new PropertyFilter(MatchType.ACLPREFIXS, "aclCode", aclCodePrefixs));
            }
            String authUserAclType = AuthContextHolder.getAuthUserDetails().getAclType();
            if (StringUtils.isNotBlank(authUserAclType)) {
                groupFilter.forceAnd(new PropertyFilter(MatchType.LE, "aclType", authUserAclType));
            }
        }
        String departmentId = getParameter("departmentId");
        if (StringUtils.isNotBlank(departmentId)) {
            String searchType = getParameter("searchType", "current");
            if ("current".equals(searchType)) {
                groupFilter.forceAnd(new PropertyFilter(MatchType.EQ, "department.id", departmentId));
            } else {
                Department current = departmentService.findOne(departmentId);
                List<Department> departments = departmentService.findChildrenCascade(current);
                departments.add(current);
                groupFilter.forceAnd(new PropertyFilter(MatchType.IN, "department", departments));
            }
        }

        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(getRequest());
        Page<User> page = this.getEntityService().findByPage(groupFilter, pageable);
        if (aclService != null) {
            Map<String, String> globalAclTypeMap = aclService.getAclTypeMap();
            for (User user : page.getContent()) {
                user.addExtraAttribute("aclTypeLabel", globalAclTypeMap.get(user.getAclType()));
            }
        }
        setModel(page);
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(value = "删除")
    public HttpHeaders doDelete() {
        if (bindingEntity.getSigninid().equals("admin")) {
            Validation.notDemoMode();
        }
        return super.doDelete();
    }

    @MetaData(value = "机构选取的Autocomplete数据")
    @SecurityControlIgnore
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

    @MetaData(value = "汇总用户关联权限集合")
    public HttpHeaders privileges() {
        List<Privilege> privileges = privilegeService.findAllCached();
        List<Role> roles = roleService.findR2RolesForUser(bindingEntity);
        boolean isAdmin = false;
        for (Role role : roles) {
            if (role.getCode().equals(Role.ROLE_ADMIN_CODE)) {
                isAdmin = true;
                break;
            }
        }

        Map<String, List<Privilege>> groupDatas = Maps.newLinkedHashMap();
        List<Privilege> r2s = null;
        if (isAdmin) {
            r2s = privileges;
        } else {
            r2s = userService.findRelatedPrivilegesForUser(bindingEntity);
        }
        for (Privilege privilege : privileges) {
            List<Privilege> groupPrivileges = groupDatas.get(privilege.getCategory());
            if (groupPrivileges == null) {
                groupPrivileges = Lists.newArrayList();
                groupDatas.put(privilege.getCategory(), groupPrivileges);
            }
            groupPrivileges.add(privilege);
            privilege.addExtraAttribute("related", false);
            if (r2s.contains(privilege)) {
                privilege.addExtraAttribute("related", true);
            }
        }
        this.getRequest().setAttribute("privileges", groupDatas);
        return buildDefaultHttpHeaders("privileges");
    }

    @MetaData(value = "汇总用户关联菜单集合")
    public HttpHeaders menus() {
        Set<GrantedAuthority> authsSet = new HashSet<GrantedAuthority>();
        List<Role> roles = roleService.findR2RolesForUser(bindingEntity);
        for (Role role : roles) {
            authsSet.add(new SimpleGrantedAuthority(role.getCode()));
        }
        List<NavMenuVO> menus = menuService.authUserMenu(authsSet, this.getRequest().getContextPath());
        setModel(menus);
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(value = "版本数据列表")
    public HttpHeaders revisionList() {
        return super.revisionList();
    }

    @Override
    @MetaData(value = "版本数据对比")
    public HttpHeaders revisionCompare() {
        return super.revisionCompare();
    }

    public HttpHeaders resetActivitiIndentityData() {
        userService.resetActivitiIndentityData();
        setModel(OperationResult.buildSuccessResult("工作流用户群组数据重置操作完成"));
        return buildDefaultHttpHeaders();
    }
}
