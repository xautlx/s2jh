package lab.s2jh.auth.web.action;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lab.s2jh.auth.entity.Privilege;
import lab.s2jh.auth.entity.Role;
import lab.s2jh.auth.entity.RoleR2Privilege;
import lab.s2jh.auth.service.PrivilegeService;
import lab.s2jh.auth.service.RoleService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.util.UidUtils;
import lab.s2jh.core.web.annotation.SecurityControlIgnore;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.sys.service.DataDictService;
import lab.s2jh.web.action.BaseController;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.config_browser.ConfigurationHelper;
import org.apache.struts2.rest.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.inject.Inject;

@MetaData(value = "权限")
public class PrivilegeController extends BaseController<Privilege, String> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DataDictService dataDictService;

    private static AntPathMatcher urlMatcher = new AntPathMatcher();

    @Override
    protected BaseService<Privilege, String> getEntityService() {
        return privilegeService;
    }

    protected ConfigurationHelper configHelper;

    @Inject
    public void setConfigurationHelper(ConfigurationHelper configHelper) {
        this.configHelper = configHelper;
    }

    @Override
    protected void checkEntityAclPermission(Privilege entity) {
        //Do nothing check
    }

    private static List<PrivilegeUrlVO> urls;

    @MetaData(value = "计算显示可控权限URL列表")
    public HttpHeaders urls() {
        if (urls == null) {
            urls = Lists.newArrayList();
            Set<String> namespaces = configHelper.getNamespaces();
            for (String namespace : namespaces) {
                if (StringUtils.isBlank(namespace) || namespace.startsWith("/pub")) {
                    continue;
                }
                Set<String> actionNames = configHelper.getActionNames(namespace);
                for (String actionName : actionNames) {
                    ActionConfig actionConfig = configHelper.getActionConfig(namespace, actionName);
                    String className = actionConfig.getClassName();
                    if (!className.startsWith("org.apache.struts")) {
                        logger.debug("Parsing actionConfig={}", actionConfig);
                        try {
                            Class<?> actionClass = ClassUtils.getClass(className);
                            String actionNameLabel = null;
                            MetaData actionMetaData = actionClass.getAnnotation(MetaData.class);
                            if (actionMetaData != null) {
                                actionNameLabel = actionMetaData.value();
                            } else {
                                Object genericClz = actionClass.getGenericSuperclass();
                                if (genericClz instanceof ParameterizedType) {
                                    Class<?> entityClass = (Class<?>) ((ParameterizedType) actionClass
                                            .getGenericSuperclass()).getActualTypeArguments()[0];
                                    MetaData entityClassMetaData = (MetaData) entityClass.getAnnotation(MetaData.class);
                                    if (entityClassMetaData != null) {
                                        actionNameLabel = entityClassMetaData.value();
                                    }
                                }
                            }

                            String namespaceLabel = null;
                            MetaData namespaceMetaData = actionClass.getPackage().getAnnotation(MetaData.class);
                            if (namespaceMetaData != null) {
                                namespaceLabel = namespaceMetaData.value();
                            } else {
                                namespaceLabel = namespace;
                            }

                            //默认添加当前NameSpace URL
                            PrivilegeUrlVO indexUrlVO = new PrivilegeUrlVO();
                            indexUrlVO.setNamespace(namespace);
                            indexUrlVO.setNamespaceLabel(namespaceLabel);
                            indexUrlVO.setActionName(actionName);
                            indexUrlVO.setActionNameLabel(actionNameLabel);
                            indexUrlVO.setMethodName("index");
                            indexUrlVO.setMethodNameLabel("主界面");
                            indexUrlVO.setUrl(namespace + "/" + actionName);
                            urls.add(indexUrlVO);

                            for (Method method : actionClass.getDeclaredMethods()) {
                                SecurityControlIgnore securityControlIgnore = method
                                        .getAnnotation(SecurityControlIgnore.class);
                                if (securityControlIgnore != null) {
                                    continue;
                                }
                                PrivilegeUrlVO privilegeUrlVO = new PrivilegeUrlVO();
                                if (Modifier.isPublic(method.getModifiers())
                                        && (method.getReturnType() == String.class || method.getReturnType() == HttpHeaders.class)) {
                                    String url = namespace + "/" + actionName + "!" + method.getName();
                                    privilegeUrlVO.setNamespace(namespace);
                                    privilegeUrlVO.setNamespaceLabel(namespaceLabel);
                                    privilegeUrlVO.setActionName(actionName);
                                    privilegeUrlVO.setActionNameLabel(actionNameLabel);
                                    privilegeUrlVO.setMethodName(method.getName());
                                    MetaData methodMetaData = method.getAnnotation(MetaData.class);
                                    if (methodMetaData != null) {
                                        privilegeUrlVO.setMethodNameLabel(methodMetaData.value());
                                    }
                                    privilegeUrlVO.setUrl(url);
                                    urls.add(privilegeUrlVO);
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }

        Iterable<Privilege> privileges = privilegeService.findAllCached();
        for (PrivilegeUrlVO url : urls) {
            url.setControlled(false);
            url.setControllPrivileges(new HashSet<String>());
            for (Privilege privilege : privileges) {
                String privilegeUrl = privilege.getUrl();
                if (StringUtils.isNotBlank(privilegeUrl)) {
                    for (String splitUrl : privilegeUrl.split("\n")) {
                        if (!splitUrl.endsWith("**")) {
                            splitUrl = splitUrl + "**";
                        }
                        if (urlMatcher.match(splitUrl, url.getUrl())) {
                            url.getControllPrivileges().add(privilege.getCode() + ": " + splitUrl);
                            url.setControlled(true);
                        }
                    }
                }
            }
        }

        this.setModel(buildPageResultFromList(urls));
        return buildDefaultHttpHeaders();
    }

    public Map<String, String> getUrlMapFromParameters() {
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        String namespace = this.getParameter("namespace");
        String actionName = this.getParameter("actionName");
        String url = this.getParameter("url");
        if (StringUtils.isNotBlank(namespace)) {
            dataMap.put(namespace, namespace);
        }
        dataMap.put(namespace + "/" + actionName, namespace + "/" + actionName);
        dataMap.put(url, url);
        return dataMap;
    }

    @MetaData("去重权限分类数据")
    public HttpHeaders distinctCategories() {
        List<String> categories = privilegeService.findDistinctCategories();
        setModel(categories);
        return buildDefaultHttpHeaders();
    }

    /**
     * 用于计算存储基于Controller方法列表的URL列表数据VO对象
     * 
     */
    public static class PrivilegeUrlVO {
        private String id = UidUtils.UID();
        private String namespace;
        private String namespaceLabel;
        private String actionName;
        private String actionNameLabel;
        private String methodName;
        private String methodNameLabel;
        private String url;
        private boolean controlled = false;
        private Set<String> controllPrivileges;

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getActionName() {
            return actionName;
        }

        public void setActionName(String actionName) {
            this.actionName = actionName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isControlled() {
            return controlled;
        }

        public void setControlled(boolean controlled) {
            this.controlled = controlled;
        }

        public Set<String> getControllPrivileges() {
            return controllPrivileges;
        }

        public void setControllPrivileges(Set<String> controllPrivileges) {
            this.controllPrivileges = controllPrivileges;
        }

        public String getControllPrivilegesJoin() {
            return StringUtils.join(controllPrivileges, "<br>");
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getActionNameLabel() {
            return actionNameLabel;
        }

        public void setActionNameLabel(String actionNameLabel) {
            this.actionNameLabel = actionNameLabel;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getMethodNameLabel() {
            return methodNameLabel;
        }

        public void setMethodNameLabel(String methodNameLabel) {
            this.methodNameLabel = methodNameLabel;
        }

        public String getNamespaceLabel() {
            return namespaceLabel;
        }

        public void setNamespaceLabel(String namespaceLabel) {
            this.namespaceLabel = namespaceLabel;
        }
    }

    @MetaData(value = "计算显示角色关联数据")
    @SecurityControlIgnore
    public HttpHeaders roles() {
        List<Role> roles = roleService.findAllCached();
        List<RoleR2Privilege> r2s = privilegeService.findRelatedRoleR2PrivilegesForPrivilege(this.getId());
        for (Role role : roles) {
            role.addExtraAttribute("related", false);
            for (RoleR2Privilege r2 : r2s) {
                if (r2.getRole().equals(role)) {
                    role.addExtraAttribute("r2CreatedDate", r2.getCreatedDate());
                    role.addExtraAttribute("related", true);
                    break;
                }
            }
        }

        this.getRequest().setAttribute("roles", roles);
        return buildDefaultHttpHeaders("roles");
    }

    @MetaData(value = "更新角色关联")
    @SecurityControlIgnore
    public HttpHeaders doUpdateRelatedRoleR2s() {
        privilegeService.updateRelatedRoleR2s(getId(), getParameterIds("r2ids"));
        setModel(OperationResult.buildSuccessResult("更新角色关联操作完成"));
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(value = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
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
    public void prepareCreate() {
        super.prepareCreate();
        bindingEntity.setCode("P" + RandomStringUtils.randomNumeric(6));
    }

}
