package lab.s2jh.core.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 用于代码工具生成框架代码的标识注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface EntityAutoCode {
    
    /**
     * 查询区域生成标识
     * @return
     */
    boolean search() default false;
    
    /**
     * 高级查询区域生成标识
     * @return
     */
    boolean searchAdvance() default false;
    
    /**
     * 生成Grid列表元素并设置为hidden=false
     * @return
     */
    boolean listShow() default true;
    
    /**
     * 生成Grid列表元素并设置为hidden=true
     * @return
     */
    boolean listHidden() default false;

    /**
     * 生成编辑界面元素标识
     * @return
     */
    boolean edit() default true;
    
    /**
     * 元素显示顺序
     * @return
     */
    int order() default Integer.MAX_VALUE;
    
    /**
     * 属性是否用于动态显示对比视图
     * @return
     */
    boolean comparable() default true;
}
