package org.gudian.web.result.handler;

import org.gudian.web.exception.MyException;
import org.gudian.web.result.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author GJW
 * : 2020/12/24 9:12
 */
@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(MyException.class)
    public ResponseResult handler(MyException e){
        e.printStackTrace();
        return e.getResultStatus().getResponseResult();
    }

}
