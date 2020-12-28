package org.gudian.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.gudian.log.annotations.SystemLog;
import org.gudian.utils.IPUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author GJW
 * @time: 2020/12/28 15:32
 */
@Component
@Aspect
public class SystemLogAop {

    @Pointcut(" @annotation(systemLog) ")
    public void pointCut(SystemLog systemLog){};


    @Around(value = "pointCut(systemLog)")
    public Object LogAround(ProceedingJoinPoint joinPoint, SystemLog systemLog){
        SystemLogDto systemLogDto = new SystemLogDto();

        //该方法的类名
        systemLogDto.setClassName( joinPoint.getTarget().getClass().getName() );

        //该方法的方法名
        systemLogDto.setMethodName( joinPoint.getSignature().getName() );

        //操作时间
        systemLogDto.setOperationTime( new Date() );

        //操作类型
        systemLogDto.setOperationType( systemLog.type() );

        //描述
        systemLogDto.setDescription( systemLog.value() );

        //获取访问的ip
        systemLogDto.setIpAddress( IPUtils.getIpAddr() );


        //方法执行耗时
        Long time = 0L;


        Object result = null;
        try {
            Long begin = System.currentTimeMillis();
            result = joinPoint.proceed(joinPoint.getArgs());
            time = System.currentTimeMillis()-begin;

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            systemLogDto.setSuccess(false);
        }
        systemLogDto.setSuccess(false);


        return result;
    }

//    public String buildParameters(ProceedingJoinPoint joinPoint){
//
//    }

}
