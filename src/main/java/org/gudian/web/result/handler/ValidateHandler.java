package org.gudian.web.result.handler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.gudian.web.result.ResponseResult;
import org.gudian.web.result.ResultStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author GJW
 * 拦截Controller层 JSR303校验异常，统一返回错误信息
 * time: 2021/1/12 11:34
 */
@Aspect
@Component
public class ValidateHandler {


    @Pointcut(" execution( * com.mt.controller..*.*(..) )")
    public void pointCut(){}


    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        //判断bindingResult 内是否有错误
        for (Object arg : args) {
            if(arg instanceof BindingResult && ((BindingResult) arg).hasFieldErrors()) {
                //处理异常
                return handlerError(  (BindingResult) arg  );
            }
        }

        return joinPoint.proceed(args);
    }

    /**
     * 处理异常
     * */
    private ResponseResult handlerError(BindingResult bindingResult) {
        FieldError fieldError = bindingResult.getFieldError();
        return ResultStatus.VALIDATEFAILED.setMsg( fieldError.getDefaultMessage() );
    }


}
