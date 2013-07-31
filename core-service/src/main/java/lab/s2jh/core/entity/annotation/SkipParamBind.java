package lab.s2jh.core.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lab.s2jh.core.web.interceptor.ExtParametersInterceptor;

/**
 * @see ExtParametersInterceptor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface SkipParamBind {

}
