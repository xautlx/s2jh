package lab.s2jh.core.web.interceptor;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.entity.annotation.SkipParamBind;
import lab.s2jh.core.exception.WebException;
import lab.s2jh.core.web.PersistableController;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Persistable;

import com.google.common.collect.Sets;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.interceptor.ParametersInterceptor;
import com.opensymphony.xwork2.util.CompoundRoot;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 1，Struts的参数绑定机制可以大幅简化参数处理工作量,但是会存在潜在的被攻击风险:用户发起一些非法的请求提交一些不必要的参数绑定从而导致非法的数据修改
 * 此处为ParametersInterceptor扩展添加基于Annotation注解的Skip参数设定,对于一些敏感的实体属性可添加@see ParamBindIgnore
 * 以跳过Struts的对此属性的自动绑定从而避免恶意属性值修改
 * 2，全局反射处理一对一关联对象“清除”处理动作、一对多的关联对象元素“remove”处理动作。
 * TODO: 此处理主要是为了以通用模型简化开发编码量，但是可能存在一定性能损耗，具体损耗程度需要进一步性能测试验证。
 * 如果开发应用比较介意性能因素可以考虑去掉通用处理逻辑，在各业务代码中定制化实现。
 */
public class ExtParametersInterceptor extends ParametersInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ExtParametersInterceptor.class);

    protected boolean isAccepted(String paramName) {
        boolean matches = super.isAccepted(paramName);
        if (matches) {
            try {
                CompoundRoot root = ActionContext.getContext().getValueStack().getRoot();
                for (Object obj : root) {
                    if (obj != null && BaseEntity.class.isAssignableFrom(obj.getClass())) {
                        Method method = OgnlRuntime.getSetMethod((OgnlContext) ActionContext.getContext()
                                .getContextMap(), obj.getClass(), paramName);
                        if (method != null) {
                            SkipParamBind mvcAutoBindExclude = method.getAnnotation(SkipParamBind.class);
                            if (mvcAutoBindExclude != null) {
                                matches = false;
                                logger.info(
                                        "Skip auto bind parameter to model property according MvcAutoBind annotation: {} : {}",
                                        obj.getClass(), paramName);
                            }
                        }
                    }
                }
            } catch (IntrospectionException e) {
                logger.error(e.getMessage(), e);
            } catch (OgnlException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return matches;
    }

    protected Map<String, Object> retrieveParameters(ActionContext ac) {
        Map<String, Object> params = ac.getParameters();
        //id参数移除，不自动绑定，采用标准的request.getParameter获取参数并查询实体对象
        params.remove("id");
        return params;
    }

    @SuppressWarnings("rawtypes")
    protected void setParameters(final Object action, ValueStack stack, final Map<String, Object> parameters) {
        super.setParameters(action, stack, parameters);

        if (action instanceof PersistableController) {
            HttpServletRequest request = ServletActionContext.getRequest();
            if (request.getMethod().equalsIgnoreCase("POST")) {
                PersistableController pc = (PersistableController) action;
                Object model = pc.getModel();
                if (model != null && model instanceof Persistable) {
                    try {
                        Persistable entity = (Persistable) model;
                        Set<String> needRemoveElementsPropertyNames = Sets.newHashSet();
                        for (String key : parameters.keySet()) {
                            if (key.endsWith(".id")) {
                                //对于关联对象，由于Struts默认设置为New一个对象实例以进行后续的参数数据绑定
                                 //在把关联OneToOne对象修改为为空时，需要做个特殊处理以把“空数据”的示例对象重置回null
                                 //以避免JPA做对象实例merge操作时抛出未保存实体对象错误
                                String name = StringUtils.substringBeforeLast(key, ".id");
                                String value = request.getParameter(key);
                                //id有值，则无需处理继续循环
                                if (StringUtils.isNotBlank(value)) {
                                    continue;
                                }

                                //计数器判断当前对象是多个对象数据提交还是简单的只是关联对象提交
                                //.display一般用于下拉或combox选取输入表单元素，不算做有效的对象数据提交内容
                                //如果出现除此之外的关联属性数据，说明是关联对象数据变更处理，则继续后续的对象级联保存处理
                                int cnt = 0;
                                for (String param : parameters.keySet()) {
                                    if (param.startsWith(name + ".") && !param.equals(name + ".display")
                                            && !param.equals(name + ".id")) {
                                        cnt++;
                                        break;
                                    }
                                }

                                //如果计数器为0则说明当前不是对象数据编辑模式，而是简单的关联对象处理模式
                                if (cnt == 0) {
                                    logger.debug("Reset [{}] OneToOne [{}] to null as empty id value", model, name);
                                    if (name.indexOf("[") > -1 && name.indexOf("]") > -1) {
                                        //集合类型属性
                                        String fisrtPropName = StringUtils.substringBefore(name, "[");
                                        int idx = Integer.valueOf(StringUtils.substringBetween(name, "[", "]"));
                                        List items = (List) MethodUtils.invokeMethod(model,
                                                "get" + StringUtils.capitalize(fisrtPropName), null);
                                        String lastPropName = StringUtils.substringAfter(name, "]");
                                        if (StringUtils.isBlank(lastPropName)) {
                                            items.remove(idx);
                                        } else {
                                            Object item = items.get(idx);
                                            //TODO 目前只支持单层，需要添加多层嵌套处理逻辑
                                            String fieldName = StringUtils.substringAfter(lastPropName, ".");
                                            if (FieldUtils.getDeclaredField(item.getClass(), fieldName, true) != null) {
                                                FieldUtils.writeDeclaredField(item, fieldName, null, true);
                                            }
                                        }
                                    } else {
                                        //单一属性，直接把关联对象设置为null，否则不做处理传递到后端会认为是一个没有任何数据但是需要新创建的级联处理对象
                                        //TODO 目前只支持单层，需要添加多层嵌套处理逻辑
                                        if (FieldUtils.getDeclaredField(model.getClass(), name, true) != null) {
                                            FieldUtils.writeDeclaredField(model, name, null, true);
                                        }
                                    }
                                }
                            } else if (key.endsWith(".extraAttributes.operation")) {
                                //汇总需要进行remove处理的集合元素属性
                                //purchaseOrderDetails[1].extraAttributes.operation=remove
                                if (!entity.isNew()) {
                                    String value = request.getParameter(key);
                                    if ("remove".equals(value)) {
                                        String name = StringUtils
                                                .substringBeforeLast(key, ".extraAttributes.operation");
                                        name = StringUtils.substringBeforeLast(name, "[");
                                        needRemoveElementsPropertyNames.add(name);
                                    }
                                }
                            }
                        }

                        //对于包含remove移除请求的集合属性进行清理处理
                        if (!entity.isNew()) {
                            for (String propName : needRemoveElementsPropertyNames) {
                                Method method = OgnlRuntime.getGetMethod(null, model.getClass(), propName);
                                Collection r2s = (Collection) method.invoke(model);
                                for (Iterator iter = r2s.iterator(); iter.hasNext();) {
                                    PersistableEntity persistable = (PersistableEntity) iter.next();
                                    if (persistable.isMarkedRemove()) {
                                        iter.remove();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        throw new WebException("error.hack.empty.onetoone.entity", e);
                    }
                }
            }
        }
    }
}
