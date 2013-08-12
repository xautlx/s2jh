package lab.s2jh.core.web;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.audit.envers.EntityRevision;
import lab.s2jh.core.audit.envers.ExtDefaultRevisionEntity;
import lab.s2jh.core.audit.envers.ExtRevisionListener;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.core.entity.def.OperationAuditable;
import lab.s2jh.core.exception.WebException;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.util.ExtStringUtils;
import lab.s2jh.core.web.view.OperationResult;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.RestActionSupport;
import org.hibernate.envers.RevisionType;
import org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public abstract class PersistableController<T extends PersistableEntity<ID>, ID extends Serializable> extends
        RestActionSupport implements ModelDriven<Object>, Preparable {

    private final Logger logger = LoggerFactory.getLogger(PersistableController.class);

    /** Autocomplete组件传递的查询参数的名称 */
    protected static final String PARAM_NAME_FOR_AUTOCOMPLETE = "term";

    /** 请求URL可提供此参数指定转向特定JSP页面，如有相同处理方法返回相同数据，但是不同业务功能需要按照不同页面显示则可以指定此参数转向特定显示JSP页面*/
    protected static final String PARAM_NAME_FOR_FORWARD_TO = "_to_";

    /** 分页查询方法特定的数据处理格式标识参数，默认标识返回查询JSON数据，可指定如xls标识导出对应的（不分页）查询数据 */
    protected static final String PARAM_NAME_FOR_EXPORT_FORMAT = "_format_";

    /** 子类指定泛型对应的实体Service接口对象 */
    abstract protected BaseService<T, ID> getEntityService();

    /** 泛型对应的Class定义 */
    protected Class<T> entityClass;

    /** 泛型对应的Class定义 */
    protected Class<ID> entityIdClass;

    /** 用于数据绑定的Entity对象实例 */
    protected T bindingEntity;

    /** 用于批量操作的数据绑定的Entity对象实例集合 */
    protected Collection<T> bindingEntities;

    protected Object model = null;

    public String getActionName() {

        //TODO 后期考虑优化为直接注入Struts的ActionNameBuilder方式
        String actionName = this.getClass().getSimpleName();
        String actionSuffix = "Controller";
        if (actionName.equals(actionSuffix))
            throw new IllegalStateException("The action name cannot be the same as the action suffix [" + actionSuffix
                    + "]");

        // Truncate Action suffix if found
        if (actionName.endsWith(actionSuffix)) {
            actionName = actionName.substring(0, actionName.length() - actionSuffix.length());
        }

        // Convert to underscores
        char[] ca = actionName.toCharArray();
        StringBuilder build = new StringBuilder("" + ca[0]);
        boolean lower = true;
        for (int i = 1; i < ca.length; i++) {
            char c = ca[i];
            if (Character.isUpperCase(c) && lower) {
                build.append("-");
                lower = false;
            } else if (!Character.isUpperCase(c)) {
                lower = true;
            }

            build.append(c);
        }

        actionName = build.toString();
        actionName = actionName.toLowerCase();

        return actionName;
    }

    /** 除ModelDriven返回的model对象以外，额外的控制参数Map结构数据，如根据业务逻辑控制页面按钮的disabled状态等 */
    private Map<String, Object> controlAttributes = new HashMap<String, Object>();

    public Map<String, Object> getControlAttributes() {
        return controlAttributes;
    }

    protected void addControlAttribute(String key, Object value) {
        controlAttributes.put(key, value);
    }

    /**
     * 初始化构造方法，计算相关泛型对象
     */
    @SuppressWarnings("unchecked")
    public PersistableController() {
        super();
        // 通过反射取得Entity的Class.
        try {
            Object genericClz = getClass().getGenericSuperclass();
            if (genericClz instanceof ParameterizedType) {
                entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
                entityIdClass = (Class<ID>) ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[1];
            }
        } catch (Exception e) {
            throw new WebException(e.getMessage(), e);
        }
    }

    // ----------------------------------  
    // -----------相关接口回调方法------------
    // ----------------------------------
    /**
     * 通用的Preparable接口prepare回调方法，如果出现id参数，则提前准备binding绑定对象用于后续方法使用
     * 同时修改了Struts2默认的PrepareInterceptor实现方式：先执行prepare再执行相关的prepareXXX方法
     * @see lab.s2jh.core.web.interceptor.ExtPrepareInterceptor
     */
    @Override
    public void prepare() {
        String id = this.getParameter("id");
        if (StringUtils.isNotBlank(id)) {
            bindingEntity = getEntityService().findOne(getId());
            if (bindingEntity != null) {
                checkEntityAclPermission(bindingEntity);
            }
        }
    }

    /**
     * 判断当前实体对象是否已持久化对象
     * 一般用于前端页面OGNL语法计算<s:property value="%{persistentedModel?'doUpdate':'doCreate'}"/>
     * @return
     */
    public boolean isPersistentedModel() {
        if (bindingEntity != null && bindingEntity.getId() != null
                && String.valueOf(bindingEntity.getId()).trim().length() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据当前登录用户对数据对象进行访问控制权限检查
     * 为了严格控制用户非法的数据访问，设计为abstract强制要求子类提供实现定义
     * @param entity 待update可操作性检查对象
     * @exception 如果检查没有权限，则直接抛出运行异常即可
     */
    protected abstract void checkEntityAclPermission(T entity);

    /**
     * ModelDriven接口回调实现方法
     */
    @Override
    public Object getModel() {
        if (model == null) {
            model = bindingEntity;
        }
        return model;
    }

    /**
     * 用于子类方法修改设置返回的ModelDriven模型对象
     * @param model
     */
    protected void setModel(Object model) {
        this.model = model;
    }

    /**
     * 构造默认的REST返回响应，一般用于直接JSON数据输出
     * @return
     */
    protected DefaultHttpHeaders buildDefaultHttpHeaders() {
        return new DefaultHttpHeaders().disableCaching();
    }

    /**
     * 基于code参数构造默认的REST返回响应，一般用于JSP页面转向显示数据
     * @return
     */
    protected DefaultHttpHeaders buildDefaultHttpHeaders(String code) {
        String to = this.getParameter(PARAM_NAME_FOR_FORWARD_TO);
        if (StringUtils.isNotBlank(to)) {
            code = to;
        }
        return new DefaultHttpHeaders(code).disableCaching();
    }

    /**
     * 帮助类方法，方便获取HttpServletRequest
     * 
     * @return
     */
    protected HttpServletRequest getRequest() {
        HttpServletRequest request = ServletActionContext.getRequest();
        return request;
    }

    /**
     * 帮助类方法，方便获取HttpServletResponse
     * 
     * @return
     */
    protected HttpServletResponse getResponse() {
        HttpServletResponse response = ServletActionContext.getResponse();
        return response;
    }

    // ----------------------------------  
    // -----------请求参数处理帮助类方法--------
    // ----------------------------------

    /**
     * 获取必须参数值,如果参数为空则抛出异常
     * 
     * @param name 参数名称
     * @return 参数值
     */
    protected String getRequiredParameter(String name) {
        String value = getRequest().getParameter(name);
        if (StringUtils.isBlank(value)) {
            throw new WebException("web.param.disallow.empty: " + name);
        }
        return value;
    }

    /**
     * 获取参数值,如果未空白则返回缺省值
     * 
     * @param name 参数名称
     * @param name 如果参数为空返回的默认值
     * @return 参数值
     */
    protected String getParameter(String name, String defaultValue) {
        String value = getRequest().getParameter(name);
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 常规方式获取请求参数值
     * 
     * @param name  参数名称
     * @return 参数值
     */
    protected String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * 将id=123格式的字符串id参数转换为ID泛型对应的主键变量实例
     * 另外，页面也会以Struts标签获取显示当前操作对象的ID值
     * @return ID泛型对象实例
     */
    @SuppressWarnings("unchecked")
    public ID getId() {
        String entityId = this.getParameter("id");
        if (StringUtils.isBlank(entityId)) {
            return null;
        }
        if (String.class.isAssignableFrom(entityIdClass)) {
            return (ID) entityId;
        } else if (Long.class.isAssignableFrom(entityIdClass)) {
            return (ID) (Long.valueOf(entityId));
        } else {
            throw new IllegalStateException("Undefine entity ID class: " + entityIdClass);
        }
    }

    // ----------------------------------  
    // -----------OGNL处理帮助类方法---------
    // ----------------------------------
    /**
     * 用于OGNL判断字符串不为Blank
     * @param str 判断字符串
     * @return
     */
    public boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    /**
     * 用于OGNL判断字符串为Blank
     * @param str 判断字符串
     * @return
     */
    public boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    // -------------------------------------
    // -----------通用的页面转向处理方法------------
    // -------------------------------------
    /**
     * 显示默认Index主界面
     * @return
     */
    public HttpHeaders index() {
        return buildDefaultHttpHeaders("index");
    }

    /**
     * 通用forwar转向方法，根据to转向到对应的JSP页面 如果to参数为空，则抛出必要参数缺失异常
     * 
     * @return
     */
    public String forward() {
        String to = this.getRequiredParameter(PARAM_NAME_FOR_FORWARD_TO);
        logger.debug("Direct forward to: {}", to);
        return to;
    }

    /**
     * 用于创建或更新时转向通用的Tabs页面
     * 一般创建是Tabs页面只有第一个Tab可编辑，其余为Disabled的状态
     * @return
     */
    public HttpHeaders inputTabs() {
        return buildDefaultHttpHeaders("inputTabs");
    }

    // -------------------------------------- 
    // -----------View查看处理相关逻辑------------
    // -------------------------------------
    /**
     * 显示查看Tabs页面
     * @return
     */
    public HttpHeaders viewTabs() {
        return buildDefaultHttpHeaders("viewTabs");
    }

    /**
     * 通用的prepare接口方法中已经实现根据ID准备好相关的binding对象
     * 如果子类需要根据其他如code代码等属性查看数据,则根据业务逻辑覆写此方法即可
     */
    public void prepareView() {

    }

    /**
     * 查看对象显示页面
     * @return
     */
    public HttpHeaders view() {
        return buildDefaultHttpHeaders("viewBasic");
    }

    // --------------------------------------  
    // -----------Create创建处理相关逻辑------------
    // --------------------------------------
    /**
     * 显示创建页面之前准备new实体对象
     */
    public void prepareCreate() {
        newBindingEntity();
    }

    /**
     * 转向创建对象录入页面
     * @return
     */
    public HttpHeaders create() {
        return buildDefaultHttpHeaders("inputBasic");
    }

    protected void newBindingEntity() {
        try {
            bindingEntity = entityClass.newInstance();
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * doCreate调用之前的Preparable接口自动回调方法
     * 准备new实体对象以备ParametersInterceptor进行参数绑定
     */
    public void prepareDoCreate() {
        newBindingEntity();
    }

    /**
     * 为了避免由于权限配置不严格，导致未授权的Controller数据操作访问，父类提供protected基础实现，子类根据需要覆写public然后调用基类方法
     * @return JSON操作提示
     */
    @MetaData(title = "创建")
    protected HttpHeaders doCreate() {
        //检查提交的数据参数符合用户ACL权限，否则拒绝创建数据
        checkEntityAclPermission(bindingEntity);
        ExtRevisionListener.setOperationEvent(RevisionType.ADD.name());
        getEntityService().save(bindingEntity);
        setModel(OperationResult.buildSuccessResult("创建操作成功", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    // ---------------------------------------  
    // -----------Update更新处理相关逻辑------------
    // ---------------------------------------
    /**
     * 更新对象显示页面，在通用的prepare接口方法中已经准备好相关的binding对象
     * 
     * @return
     */
    public HttpHeaders update() {
        return buildDefaultHttpHeaders("inputBasic");
    }

    /**
     * 检查当前对象是否禁止update更新操作
     * 默认子类已经强制实现 @see #checkEntityAclPermission 方法
     * 则子类只需根据业务需要添加checkEntityAclPermission之外的update操作检查权限逻辑
     * 一般子类其他业务方法需要更新对象，则需要根据情况调用此方法进行update更新检查
     * 此方法除了在prepareDoUpdate方法中调用进行更新检查用途外,还可以用于前端页面以OGNL方式控制"更新"操作按钮的disabled状态
     * @param entity 待update可操作性检查对象
     * @return 是否禁止更新
     */
    public boolean isDisallowUpdate() {
        return false;
    }

    /**
     * doUpdate调用之前的Preparable接口自动回调方法
     * 基类默认调用checkEntityUpdatePermission进行对象访问权限检查
     * 注意：之所以对象访问检查逻辑要放到prepare而不是对应的doXXX方法，是因为数据权限是基于数据库对象进行判断的
     * 在prepare中bindingEntity是刚从数据库取到的数据，还没有进行相关参数绑定
     * 而到了doXXX方法时的对象已经是完成了参数绑定的对象，通过此时用于权限判断的对象进行判断则有可能存在是被用户篡改权限属性值的风险
     * 同理：子类在其他业务方法处理时，如果也需要进行额外的数据检查时，也要注意此规则应该在对应的prepare回调方法中进行，而不是业务执行方法中
     */
    public void prepareDoUpdate() {
        Assert.isTrue(!isDisallowUpdate(), "数据访问权限不足");
    }

    /**
     * 为了避免由于权限配置不严格，导致未授权的Controller数据操作访问，父类提供protected基础实现，子类根据需要覆写public然后调用基类方法
     * @return JSON操作提示
     */
    @MetaData(title = "更新")
    protected HttpHeaders doUpdate() {
        ExtRevisionListener.setOperationEvent(RevisionType.MOD.name());
        getEntityService().save(bindingEntity);
        setModel(OperationResult.buildSuccessResult("更新操作成功", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    // --------------------------------------------- 
    // -----------Delete删除数据处理相关逻辑------------
    // ----------------------------------------------
    /**
     * 一般用于如删除等批量操作
     * @return id字符串集合
     */
    protected Set<String> getParameterIds() {
        return getParameterIds("ids");
    }

    /**
     * 一般用于如删除等批量操作
     * @return id字符串集合
     */
    protected Set<String> getParameterIds(String paramName) {
        HttpServletRequest request = ServletActionContext.getRequest();
        Set<String> idSet = Sets.newHashSet();
        String singleId = request.getParameter(paramName);
        if (StringUtils.isNotBlank(singleId)) {
            for (String id : singleId.split(",")) {
                String trimId = id.trim();
                if (StringUtils.isNotBlank(trimId)) {
                    idSet.add(trimId);
                }
            }
        }
        return idSet;
    }

    /**
     * 将ids=123,234,345等格式参数按照逗号切分并转换查询对应的Entity对象集合，方便使用
     * 一般用于如删除等批量操作
     * @return 实体对象集合
     */
    @SuppressWarnings("unchecked")
    protected Collection<T> getEntitiesByParameterIds() {
        Collection<T> entities = new ArrayList<T>();
        for (String id : getParameterIds()) {
            Object realId = null;
            if (String.class.isAssignableFrom(entityIdClass)) {
                realId = id;
            } else if (Long.class.isAssignableFrom(entityIdClass)) {
                realId = Long.valueOf(id);
            } else {
                throw new IllegalStateException("Undefine entity ID class: " + entityIdClass);
            }
            T entity = getEntityService().findOne((ID) realId);
            entities.add(entity);
        }
        return entities;
    }

    /**
     * 检查当前对象是否禁止delete更新操作
     * 默认子类已经强制实现 @see #checkEntityAclPermission 方法
     * 则子类只需根据业务需要添加checkEntityAclPermission之外的delete操作检查权限逻辑
     * 一般子类其他业务方法需要删除对象，则需要根据情况调用此方法进行delete检查
     * 此方法除了在delete方法中调用进行更新检查用途外,还可以用于前端页面以OGNL方式控制"删除"操作按钮的disabled状态
     * @param entity 待delete可操作性检查对象
     * @return 是否禁止删除
     */
    protected boolean isDisallowDelete(T entity) {
        return entity.isNew();
    }

    /**
     * 为了避免由于权限配置不严格，导致未授权的Controller数据操作访问，父类提供protected基础实现，子类根据需要覆写public然后调用基类方法
     * @return JSON操作提示
     */
    @MetaData(title = "删除")
    protected HttpHeaders doDelete() {
        Set<T> enableDeleteEntities = Sets.newHashSet();
        Collection<T> entities = this.getEntitiesByParameterIds();
        for (T entity : entities) {
            //添加检查逻辑：当前对象是否允许被删除，如状态检查
            if (!isDisallowDelete(entity)) {
                enableDeleteEntities.add(entity);
            }
        }
        ExtRevisionListener.setOperationEvent(RevisionType.DEL.name());
        //对于批量删除,循环每个对象调用Service接口删除,则各对象删除事务分离
        //这样可以方便某些对象删除失败不影响其他对象删除
        //如果业务逻辑需要确保批量对象删除在同一个事务则请子类覆写调用Service的批量删除接口
        for (T entity : enableDeleteEntities) {
            getEntityService().delete(entity);
        }
        int rejectSize = entities.size() - enableDeleteEntities.size();
        setModel(OperationResult.buildSuccessResult("删除操作完成.删除:" + enableDeleteEntities.size() + "条"
                + (rejectSize > 0 ? ",拒绝:" + rejectSize + "条" : "")));
        return buildDefaultHttpHeaders();
    }

    // --------------------------------------------- 
    // -----------findByPage分页查询处理相关逻辑------------
    // ----------------------------------------------
    /**
     * 分页列表显示数据
     * 为了避免由于权限配置不严格，导致未授权的Controller数据操作访问，父类提供protected基础实现，子类根据需要覆写public然后调用基类方法
     * @return JSON集合数据
     */
    @MetaData(title = "查询")
    protected HttpHeaders findByPage() {
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(getRequest());
        GroupPropertyFilter groupFilter = GroupPropertyFilter
                .buildGroupFilterFromHttpRequest(entityClass, getRequest());

        String foramt = this.getParameter(PARAM_NAME_FOR_EXPORT_FORMAT);
        if ("xls".equalsIgnoreCase(foramt)) {
            exportXlsForGrid(groupFilter, pageable.getSort());
        } else {
            setModel(this.getEntityService().findByPage(groupFilter, pageable));
        }
        return buildDefaultHttpHeaders();
    }

    /**
     * 子类额外追加过滤限制条件的入口方法，一般基于当前登录用户强制追加过滤条件
     * 注意：凡是基于当前登录用户进行的控制参数，一定不要通过页面请求参数方式传递，存在用户篡改请求数据访问非法数据的风险
     * 因此一定要在Controller层面通过覆写此回调函数或自己的业务方法中强制追加过滤条件
     * @param filters 已基于Request组装好查询条件的集合对象
     */
    protected void appendFilterProperty(GroupPropertyFilter groupPropertyFilter) {

    }

    /**
     * 一般用于把没有分页的集合数据转换组装为对应的Page对象，传递到前端Grid组件以统一的JSON结构数据显示
     * @param list 泛型集合数据
     * @return 转换封装的Page分页结构对象
     */
    protected <S> Page<S> buildPageResultFromList(List<S> list) {
        Page<S> page = new PageImpl<S>(list);
        return page;
    }

    /**
     * 如果业务功能支持对分页查询导出Excel组件，子类覆写此方法
     * 基类的findByPage会根据{@link #PARAM_NAME_FOR_EXPORT_FORMAT} 自动回调此方法进行Excel数据导出
     * @param filters 已基于Request组装好查询条件的集合对象
     * @param sort 已基于Request组装好的排序对象
     * @param groupFilter 已基于Request组装好高级查询条件的集合对象
     */
    protected void exportXlsForGrid(GroupPropertyFilter groupFilter, Sort sort) {
        throw new UnsupportedOperationException();
    }

    /**
     * 基类基于子类提供的相关参数数据, 生成JXLS报表
     * @see #exportXlsForGrid(List, Sort, GroupPropertyFilter) 此方法中基于参数组装好相关的data数据后，调用此方法生成Excel响应
     * @param dataMap
     */
    protected void exportExcel(String templateFileName, String exportFileName, Map<String, Object> dataMap) {
        //日期格式定义
        dataMap.put("dateFormatter", new SimpleDateFormat(DateUtils.DEFAULT_DATE_FORMAT));
        dataMap.put("timeFormatter", new SimpleDateFormat(DateUtils.DEFAULT_TIME_FORMAT));

        HttpServletResponse response = ServletActionContext.getResponse();
        InputStream fis = null;
        OutputStream fos = null;
        try {
            Resource resource = new ClassPathResource("/template/xls/" + templateFileName);
            logger.debug("Open template file inputstream: {}", resource.getURL());
            fis = resource.getInputStream();

            XLSTransformer transformer = new XLSTransformer();
            // generate the excel workbook according to the template and
            // parameters
            Workbook workbook = transformer.transformXLS(fis, dataMap);
            String filename = exportFileName;
            filename = new String(filename.getBytes("GBK"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            fos = response.getOutputStream();
            // output the generated excel file
            workbook.write(fos);
        } catch (Exception e) {
            throw new WebException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(fos);
        }
    }

    /**
     * 字段值重复性校验
     * 唯一性验证URL示例：id=1&element=masterId&masterId=ABC&additional=referenceId
     * &referenceId=XYZ 处理额外补充参数，有些数据是通过两个字段共同决定唯一性，可以通过additional参数补充提供
     */
    public HttpHeaders checkUnique() {
        String element = this.getParameter("element");
        Assert.notNull(element);
        GroupPropertyFilter groupPropertyFilter = new GroupPropertyFilter();

        String value = getRequest().getParameter(element);
        if (!ExtStringUtils.hasChinese(value)) {
            value = ExtStringUtils.encodeUTF8(value);
        }

        groupPropertyFilter.and(new PropertyFilter(entityClass, "EQ_" + element, value));

        // 处理额外补充参数，有些数据是通过两个字段共同决定唯一性，可以通过additional参数补充提供
        String additionalName = getRequest().getParameter("additional");
        if (StringUtils.isNotBlank(additionalName)) {
            String additionalValue = getRequest().getParameter(additionalName);
            if (!ExtStringUtils.hasChinese(additionalValue)) {
                additionalValue = ExtStringUtils.encodeUTF8(additionalValue);
            }
            groupPropertyFilter.and(new PropertyFilter(entityClass, additionalName, additionalValue));
        }
        String additionalName2 = getRequest().getParameter("additional2");
        if (StringUtils.isNotBlank(additionalName2)) {
            String additionalValue2 = getRequest().getParameter(additionalName2);
            if (!ExtStringUtils.hasChinese(additionalValue2)) {
                additionalValue2 = ExtStringUtils.encodeUTF8(additionalValue2);
            }
            groupPropertyFilter.and(new PropertyFilter(entityClass, additionalName2, additionalValue2));
        }

        List<T> entities = getEntityService().findByFilters(groupPropertyFilter);
        if (entities == null || entities.size() == 0) {// 未查到重复数据
            this.setModel(Boolean.TRUE);
        } else {
            if (entities.size() == 1) {// 查询到一条重复数据
                String id = getRequest().getParameter("id");
                if (StringUtils.isNotBlank(id)) {
                    Serializable entityId = entities.get(0).getId();
                    logger.debug("Check Unique Entity ID = {}", entityId);
                    if (id.equals(entityId.toString())) {// 查询到数据是当前更新数据，不算已存在
                        this.setModel(Boolean.TRUE);
                    } else {// 查询到数据不是当前更新数据，算已存在
                        this.setModel(Boolean.FALSE);
                    }
                } else {// 没有提供Sid主键，说明是创建记录，则算已存在
                    this.setModel(Boolean.FALSE);
                }
            } else {// 查询到多余一条重复数据，说明数据库数据本身有问题
                this.setModel(Boolean.FALSE);
                throw new WebException("error.check.unique.duplicate: " + element + "=" + value);
            }
        }
        return buildDefaultHttpHeaders();
    }

    private static Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();

    public String buildUidFromHttpGetRequest() throws NoSuchAlgorithmException {
        HttpServletRequest request = this.getRequest();
        String url = request.getServletPath();
        String query = request.getQueryString();
        if (StringUtils.isNotEmpty(query)) {
            url = url + "?" + query;
        }
        return md5PasswordEncoder.encodePassword(url, null);
    }

    private final static String[] excludeEntityProperties = { "id", "version", "aclCode", "aclType", "createdBy",
            "createdDate", "lastModifiedBy", "lastModifiedDate" };

    protected T cloneNewEntity(T source, String... extraExcludeProperties) {
        T cloneEntity = null;
        try {
            cloneEntity = entityClass.newInstance();
            if (extraExcludeProperties == null) {
                BeanUtils.copyProperties(source, cloneEntity, excludeEntityProperties);
            } else {
                Set<String> excludeEntityPropertiesSet = Sets.newHashSet(excludeEntityProperties);
                for (String p : extraExcludeProperties) {
                    excludeEntityPropertiesSet.add(p);
                }
                BeanUtils.copyProperties(source, cloneEntity, excludeEntityPropertiesSet.toArray(new String[] {}));
            }

        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        Assert.notNull(cloneEntity);
        return cloneEntity;
    }

    // --------------------------------------------------------
    // -----------基于Hibernate Envers的数据修改记录审计功能------------
    // --------------------------------------------------------
    /**
     * 版本数据主界面页面转向
     * @return 在struts.xml中全局的revisionIndex Result定义
     */
    public HttpHeaders revisionIndex() {
        return buildDefaultHttpHeaders("revisionIndex");
    }

    /**
     * 用于版本属性下拉列表集合
     * @return
     */
    public Map<Field, String> getRevisionFields() {
        Map<Field, String> revisionFields = Maps.newLinkedHashMap();
        for (Field field : entityClass.getDeclaredFields()) {
            EntityAutoCode entityAutoCode = field.getAnnotation(EntityAutoCode.class);
            if (entityAutoCode != null && entityAutoCode.comparable()) {
                MetaData metaData = field.getAnnotation(MetaData.class);
                revisionFields.put(field, metaData != null ? metaData.title() : field.getName().toUpperCase());
            }
        }
        return revisionFields;
    }

    /**
     * Revision操作记录列表
     * 为了避免由于权限配置不严格，导致未授权的Controller数据操作访问，父类提供protected基础实现，子类根据需要覆写public然后调用基类方法
     * @return JSON集合数据
     */
    @MetaData(title = "版本数据列表")
    protected HttpHeaders revisionList() {
        String property = this.getParameter("property");
        Boolean hasChanged = null;
        String changed = this.getParameter("changed");
        if (StringUtils.isNotBlank(changed)) {
            hasChanged = BooleanUtils.toBooleanObject(changed);
        }
        List<EntityRevision> entityRevisions = getEntityService().findEntityRevisions(this.getId(), property,
                hasChanged);
        for (EntityRevision entityRevision : entityRevisions) {
            Object entity = entityRevision.getEntity();
            ExtDefaultRevisionEntity revEntity = entityRevision.getRevisionEntity();
            if (entity instanceof OperationAuditable) {
                OperationAuditable aae = (OperationAuditable) entity;
                revEntity.setOldStateDisplay(aae.convertStateToDisplay(revEntity.getOldState()));
                revEntity.setNewStateDisplay(aae.convertStateToDisplay(revEntity.getNewState()));
                revEntity.setOperationEventDisplay(aae.convertEventToDisplay(revEntity.getOperationEvent()));
            }
        }
        setModel(buildPageResultFromList(entityRevisions));
        return buildDefaultHttpHeaders();
    }

    /**
     * Revision版本数据对比显示
     * 为了避免由于权限配置不严格，导致未授权的Controller数据操作访问，父类提供protected基础实现，子类根据需要覆写public然后调用基类方法
     * @return 在struts.xml中全局的revisionCompare Result定义
     */
    @MetaData(title = "版本数据对比")
    protected HttpHeaders revisionCompare() {
        HttpServletRequest request = this.getRequest();
        String id = String.valueOf(this.getId());
        Long revLeft = Long.valueOf(this.getRequiredParameter("revLeft"));
        Long revRight = Long.valueOf(this.getRequiredParameter("revRight"));
        EntityRevision revLeftEntity = null;
        EntityRevision revRightEntity = null;
        List<EntityRevision> entityRevisions = getEntityService().findEntityRevisions(id, revLeft, revRight);
        for (EntityRevision entityRevision : entityRevisions) {
            if (entityRevision.getRevisionEntity().getRev().equals(revLeft)) {
                revLeftEntity = entityRevision;
            } else if (entityRevision.getRevisionEntity().getRev().equals(revRight)) {
                revRightEntity = entityRevision;
            }
        }

        List<Map<String, String>> revEntityProperties = Lists.newArrayList();
        for (Map.Entry<Field, String> me : getRevisionFields().entrySet()) {
            Field field = me.getKey();
            Map<String, String> revEntityProperty = Maps.newHashMap();
            revEntityProperty.put("name", me.getValue());
            if (revLeftEntity != null) {
                try {
                    Object value = FieldUtils.readDeclaredField(revLeftEntity.getEntity(), field.getName(), true);
                    String valueDisplay = convertPropertyDisplay(revLeftEntity.getEntity(), field, value);
                    revEntityProperty.put("revLeftPropertyValue", valueDisplay);
                } catch (IllegalAccessException e) {
                    throw new WebException(e.getMessage(), e);
                }
            }
            if (revRightEntity != null) {
                try {
                    Object value = FieldUtils.readDeclaredField(revRightEntity.getEntity(), field.getName(), true);
                    String valueDisplay = convertPropertyDisplay(revRightEntity.getEntity(), field, value);
                    revEntityProperty.put("revRightPropertyValue", valueDisplay);
                } catch (IllegalAccessException e) {
                    throw new WebException(e.getMessage(), e);
                }

            }
            revEntityProperties.add(revEntityProperty);
        }
        request.setAttribute("revLeftEntity", revLeftEntity);
        request.setAttribute("revRightEntity", revRightEntity);
        request.setAttribute("revEntityProperties", revEntityProperties);
        return buildDefaultHttpHeaders("revisionCompare");
    }

    /**
     * 将对象Value对象转换为显示字符串，子类可根据需要覆写此方法输出定制格式字符串
     * @param entity 版本数据实体对象
     * @param field 版本字段属性
     * @param value 版本属性数据值
     * @return 格式化后处理的字符串
     */
    protected String convertPropertyDisplay(Object entity, Field field, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof PersistableEntity) {
            @SuppressWarnings("rawtypes")
            PersistableEntity persistableEntity = (PersistableEntity) value;
            String label = "N/A";
            try {
                label = persistableEntity.getDisplayLabel();
            } catch (EntityNotFoundException e) {
                //Hibernate Envers默认始终查询对应Audit版本数据，有可能关联对象之前没有Audit记录，从而会导致Envers抛出未找到数据异常
                //此处做Hack处理：如果没有找到关联Audit记录，则查询关联主对象记录
                try {
                    //从Hibernate AOP增强对象反查对应实体对象数据
                    JavassistLazyInitializer jli = (JavassistLazyInitializer) FieldUtils.readDeclaredField(value,
                            "handler", true);
                    Class entityClass = jli.getPersistentClass();
                    Serializable id = jli.getIdentifier();
                    Object obj = getEntityService().findEntity(entityClass, id);
                    PersistableEntity auditTargetEntity = (PersistableEntity) obj;
                    label = auditTargetEntity.getDisplayLabel();
                } catch (IllegalAccessException iae) {
                    logger.warn(e.getMessage());
                }
            }
            return label;
        }
        return String.valueOf(value);
    }
}
