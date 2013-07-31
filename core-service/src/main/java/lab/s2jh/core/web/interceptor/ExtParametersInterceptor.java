package lab.s2jh.core.web.interceptor;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.util.Map;

import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.SkipParamBind;
import lab.s2jh.core.web.BaseController;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.interceptor.ParametersInterceptor;
import com.opensymphony.xwork2.util.CompoundRoot;

/**
 * Struts的参数绑定机制可以大幅简化参数处理工作量,但是会存在潜在的被攻击风险:用户发起一些非法的请求提交一些不必要的参数绑定从而导致非法的数据修改
 * 此处为ParametersInterceptor扩展添加基于Annotation注解的Skip参数设定,对于一些敏感的实体属性可添加@see ParamBindIgnore
 * 以跳过Struts的对此属性的自动绑定从而避免恶意属性值修改
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
}
