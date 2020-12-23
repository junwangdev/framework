package org.gudian.result;

import lombok.Getter;

/**
 *  app接口返回参数
 * @author 郭俊旺
 *  2020/12/22 19:01
 */
@Getter
public class ResponseResult<T> {

    private Integer code;
    private String msg;
    private T data;


    private ResponseResult(){}

     protected static ResponseResult newInstance(ResultStatus resultStatus){
         ResponseResult responseResult = new ResponseResult();
         responseResult.code = resultStatus.getCode();
         responseResult.msg = resultStatus.getMessage();
         return responseResult;
     }

     public ResponseResult setData(T o){
        this.data = o;
        return this;
     }

     public ResponseResult setMsg(String msg){
        this.msg = msg;
        return this;
     }

}
