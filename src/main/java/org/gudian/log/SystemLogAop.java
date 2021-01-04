package org.gudian.log;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.gudian.log.annotations.SystemLog;
import org.gudian.utils.IPUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author GJW
 *  2020/12/28 15:32
 */
@ConditionalOnBean(LogHandler.class)
@Component
@Aspect
public class SystemLogAop {

    @Autowired(required = false)
    private LogHandler logHandler;

    @Autowired
    private ObjectMapper objectMapper;



    @Pointcut(" @annotation(systemLog) ")
    public void pointCut(SystemLog systemLog){}


    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,10,2, TimeUnit.SECONDS,new LinkedBlockingDeque<>(20));

    @Around(value = "pointCut(systemLog)")
    public Object logAround(ProceedingJoinPoint joinPoint, SystemLog systemLog) throws JsonProcessingException {

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


        Object result = null;
        try {
            long begin = System.currentTimeMillis();
            result = joinPoint.proceed(joinPoint.getArgs());

            //设置方法耗时
            systemLogDto.setTime( System.currentTimeMillis() - begin );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //设置操作不成功
            systemLogDto.setSuccess(false);
        }
        //设置操作成功
        systemLogDto.setSuccess(false);

        //设置调用时的参数
        systemLogDto.setParameter( buildParameters(joinPoint) );

        //设置返回值
        systemLogDto.setResult( buildResult(result) );


        //调用 LogHandler 处理日志
        threadPoolExecutor.execute( ()-> logHandler.handler(systemLogDto) );

        return result;
    }

    /**
     * 构建参数string
     * */
    private String buildParameters(ProceedingJoinPoint proceedingJoinPoint) throws JsonProcessingException {
        StringBuilder sb = new StringBuilder();

        //获取当前方法的SystemLog节点
        MethodSignature signature = (MethodSignature)proceedingJoinPoint.getSignature();
        SystemLog annotation = signature.getMethod().getAnnotation(SystemLog.class);

        //获取该方法的参数
        Parameter[] parameters = getMethodParam(proceedingJoinPoint);

        //获取需要保存的参数
        int[] params = annotation.params();
        //转换为list
        ArrayList paramsIndex = new ArrayList( Arrays.asList( ArrayUtils.toObject(params) ) );


        for(int i=0; i<parameters.length; i++){
            //如果注解没声明要保存声明参数或者声明了要保存当前下标的参数，则保存
            if(paramsIndex.contains(parameters[i]) || paramsIndex.isEmpty()){
                String paramName = getParamName(parameters[i]);
                String paramValue = getParamValue( proceedingJoinPoint.getArgs()[i]);
                sb.append(paramName);
                sb.append("->");
                sb.append(paramValue);
                sb.append("\n");
            }
        }
        return sb.toString();
    }



    /**
     * 构建返回值
     * */
    private String buildResult(Object result) throws JsonProcessingException {

        if( result ==null ){
            return "null";
        }
        //如果是简单类型
        boolean isSimple = BeanUtils.isSimpleProperty(result.getClass());


        return isSimple ? result.toString() : objectMapper.writeValueAsString( result );
    }


    /**
     * 获取参数的值
     * */
    private String getParamValue(Object args) throws JsonProcessingException {
        if(args == null){
            return "null";
        }
        //如果是简单类型
        if(BeanUtils.isSimpleProperty(args.getClass())){
            return args.toString();
        }
        return objectMapper.writeValueAsString(args);
    }

    /**
     * 获取方法的参数
     * */
    private Parameter[] getMethodParam(ProceedingJoinPoint proceedingJoinPoint){
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        return signature.getMethod().getParameters();
    }


    private String getParamName(Parameter parameter){

        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);

        //如果声明了 RequestParam 则获取里面的name
        if( requestParam !=null &&  !StrUtil.isAllBlank(requestParam.name(),requestParam.value()) ){
            return requestParam.name().isEmpty()? requestParam.value() : requestParam.name();
        }

        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        //如果声明了 PathVariable 则获取里面的name
        if( pathVariable!=null && !StrUtil.isAllBlank(pathVariable.name(),pathVariable.value())  ){
            return pathVariable.name().isEmpty() ? pathVariable.value() : pathVariable.name();
        }

        RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
        //如果声明了requestBody
        if(requestBody!=null){
            return "requestBody";
        }

        RequestHeader requestHeader = parameter.getAnnotation(RequestHeader.class);
        //如果声明了 RequestHeader
        if( requestHeader!=null && !StrUtil.isAllBlank(requestHeader.name(),requestHeader.value()) ){
            return requestHeader.name().isEmpty() ? requestHeader.value() : requestHeader.name();
        }

        return parameter.getName();
    }


}

