package lab.s2jh.biz.xs.imp.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import lab.s2jh.biz.sys.entity.EnumType.EnumTypes;
import lab.s2jh.biz.xs.imp.validation.impl.EnumTypeValueValidator;


@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumTypeValueValidator.class)
@Documented
public @interface EnumTypeValue {
    
    String message() default "未定义的枚举数据";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    EnumTypes value();
}