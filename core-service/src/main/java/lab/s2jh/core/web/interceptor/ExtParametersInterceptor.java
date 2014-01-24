package lab.s2jh.core.web.interceptor;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.entity.annotation.SkipParamBind;
import lab.s2jh.core.exception.WebException;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.PersistableController;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
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

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

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
        //把id为空白字符串情况清理掉，避免由于空字符串传给Hibernate主键判断处理异常：detached entity passed to persist
        Map<String, Object> params = ac.getParameters();
        Object idValue = params.get("id");
        if (idValue != null) {
            if (idValue instanceof String) {
                if (StringUtils.isBlank(String.valueOf(idValue))) {
                    params.remove("id");
                }
            } else {
                Object[] ids = (Object[]) idValue;
                if (ids.length == 0 || StringUtils.isBlank(String.valueOf(ids[0]))) {
                    params.remove("id");
                }
            }
        }
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
                        Set<String> needRemoveElementsPropertyNames = Sets.newHashSet();
                        for (String key : parameters.keySet()) {
                            if (key.endsWith(".id")) {
                                /**
                                 * 对于关联对象，由于Struts默认设置为New一个对象实例以进行后续的参数数据绑定
                                 * 在把关联OneToOne对象修改为为空时，需要做个特殊处理以把“空数据”的示例对象重置回null
                                 * 以避免JPA做对象实例merge操作时抛出未保存实体对象错误
                                 */
                                String name = StringUtils.substringBeforeLast(key, ".id");
                                if (name.indexOf(".") > -1) {
                                    continue;
                                }
                                String value = request.getParameter(key);
                                if (StringUtils.isNotBlank(value)) {
                                    continue;
                                }
                                Method method = OgnlRuntime.getGetMethod(null, model.getClass(), name);

                                JoinColumn joinColumn = method.getAnnotation(JoinColumn.class);
                                if (joinColumn.nullable() == false) {
                                    continue;
                                }
                                OneToOne oneToOne = method.getAnnotation(OneToOne.class);
                                if (oneToOne != null && oneToOne.optional() == false) {
                                    continue;
                                }
                                ManyToOne manyToOne = method.getAnnotation(ManyToOne.class);
                                if (manyToOne != null && manyToOne.optional() == false) {
                                    continue;
                                }

                                CascadeType[] cascadeTypes = null;
                                if (oneToOne != null) {
                                    cascadeTypes = oneToOne.cascade();
                                } else if (manyToOne != null) {
                                    cascadeTypes = manyToOne.cascade();
                                }

                                if (cascadeTypes == null || cascadeTypes.length == 0
                                        || ArrayUtils.contains(cascadeTypes, CascadeType.DETACH)) {
                                    BaseEntity one = (BaseEntity) method.invoke(model);
                                    if (one != null && one.getCreatedDate() != null) {
                                        if (one.getId() == null || one.getId().toString().trim() == "") {
                                            logger.debug("Reset [{}] OneToOne [{}] to null as empty id value", model,
                                                    name);
                                            FieldUtils.writeDeclaredField(model, name, null, true);
                                        }
                                    }
                                }
                            } else if (key.endsWith(".extraAttributes.operation")) {
                                //汇总需要进行remove处理的集合元素属性
                                //purchaseOrderDetails[1].extraAttributes.operation=remove
                                String value = request.getParameter(key);
                                if ("remove".equals(value)) {
                                    String name = StringUtils.substringBeforeLast(key, ".extraAttributes.operation");
                                    name = StringUtils.substringBeforeLast(name, "[");
                                    needRemoveElementsPropertyNames.add(name);
                                }

                            }
                        }

                        //对于包含remove移除请求的集合属性进行清理处理
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
                    } catch (Exception e) {
                        throw new WebException("error.hack.empty.onetoone.entity", e);
                    }
                }

            }

        }
    }
}
