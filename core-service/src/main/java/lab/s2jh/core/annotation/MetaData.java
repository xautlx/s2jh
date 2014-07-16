package lab.s2jh.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lab.s2jh.core.web.PersistableController;

/**
 * 用于注解类或属性的元数据，这些元数据可用于代码生成或运行时动态内容生成
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PACKAGE })
public @interface MetaData {

    /**
     * 简要注解说明：一般对应表单项Label属性显示
     */
    String value();

    /**
     * 提示信息：一般对应表单项的提示说明，支持以HTML格式
     */
    String tooltips() default "";

    /**
     * 注释说明：用于描述代码内部用法说明，一般不用于前端UI显示
     */
    String comments() default "";

    /**
     * 标识属性是否出现在版本比较列表
     * @see PersistableController#getRevisionFields()
     */
    boolean comparable() default true;
}
