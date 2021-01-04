package org.gudian.log.annotations;

import org.gudian.log.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author GJW
 *  2020/12/28 15:34
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

    /**
     * 保存对应的参数 1，3，2 表示对方法传入的前3个参数进行保存
     * 不写默认保存所有
     * */
    int[] params() default {};
}
