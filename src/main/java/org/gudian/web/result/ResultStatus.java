package org.gudian.web.result;

import lombok.Getter;

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
    NOLOGIN(403,"未登录或登陆状态已过期"),
    NOAUTHORITY(403,"对不起，您无权访问"),
    VALIDATEFAILED(415,"请求的格式错误");


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
