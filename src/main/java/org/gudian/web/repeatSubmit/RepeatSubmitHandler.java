package org.gudian.web.repeatSubmit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.gudian.security.JwtTokenUtil;
import org.gudian.web.result.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Optional;

/**
 * @author GJW
 * time: 2021/1/12 14:07
 */
@ConditionalOnBean(RedisTemplate.class)
@Aspect
@Component
public class RepeatSubmitHandler {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    private final  String cachePackage = "RsKey::";


    @Pointcut(" @annotation(noRepeatSubmit) ")
    public void pointcut(NoRepeatSubmit noRepeatSubmit){}


    @Around(" pointcut(noRepeatSubmit) ")
    public Object around(ProceedingJoinPoint joinPoint,NoRepeatSubmit noRepeatSubmit) throws Throwable {
        //时间
        final long time = noRepeatSubmit.time();
        //缓存key
        final String cacheKey = genCacheKey(joinPoint);

        ValueOperations valueOp = redisTemplate.opsForValue();

        Boolean setSuccess = valueOp.setIfAbsent(cacheKey, "lock", Duration.ofMillis(time));

        //如果设置失败
        if (!setSuccess) {
            return ResultStatus.RepeatSubmit.getResponseResult();
        }

        return joinPoint.proceed(joinPoint.getArgs());
    }


    /**
     * 生成 key
     * */
    private String genCacheKey( ProceedingJoinPoint joinPoint){

        StringBuilder strBuilder = new StringBuilder(cachePackage);

        String className  = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Long userId = Optional.ofNullable(jwtTokenUtil.getCurrentUserId()).orElse(0L);

        strBuilder.append("-").append(className);
        strBuilder.append(".").append(methodName);
        strBuilder.append("-").append(userId);

        return strBuilder.toString();
    }

}
