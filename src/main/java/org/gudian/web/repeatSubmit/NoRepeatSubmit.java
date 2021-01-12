package org.gudian.web.repeatSubmit;

/**
 * @author GJW
 * 防止重复提交表单注解
 * time: 2021/1/12 14:05
 */
public @interface NoRepeatSubmit{

    /**
     * 毫秒
     * */
    long time();
}
