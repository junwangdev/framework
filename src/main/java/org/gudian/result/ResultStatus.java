package org.gudian.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.nio.file.AccessDeniedException;

/**
 * @author GJW
 *  2020/12/22 19:31
 */
@Getter
public enum ResultStatus{
    SUCCESS(200,"成功"),
    FAIL(0,"错误"),
    PARAMNOTMATCH(1,"参数不正确"),
    ERROR(500,"系统异常"),
    NoLogin(403,"未登录或登陆状态已过期"),
    NoAuthorize(403,"对不起，您无权访问");


    private ResultStatus(Integer code,String msg){
        this.code = code;
        this.message = msg;
        this.responseResult = ResponseResult.newInstance(this);
    }

    private Integer code;
    private String message;
    private ResponseResult responseResult;

    public ResponseResult setData(Object data){
        responseResult.setData(data);
        return this.responseResult;
    }

    public ResponseResult setMsg(String msg){
        responseResult.setMsg(msg);
        return this.responseResult;
    }

}