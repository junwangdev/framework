package org.gudian.log.annotations;

import org.gudian.log.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author GJW
 * @time: 2020/12/28 15:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SystemLog {

    /**
     * 操作类型
     * */
    OperationType type() default OperationType.OTHER;

    /**
     * 描述
     * */
    String value();

    String[] params() default {};
}
