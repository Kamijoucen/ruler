package com.kamijoucen.ruler.config.option;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImportMatchOrder {

    /**
     * 加载顺序
     *
     * @return 数字越大，加载优先级越高，数字相同，加载顺序不确定，默认1000
     */
    int order() default 1000;

}
