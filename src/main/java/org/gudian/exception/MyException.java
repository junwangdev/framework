package org.gudian.exception;

import lombok.AllArgsConstructor;
import org.gudian.result.ResultStatus;
import sun.security.provider.certpath.OCSPResponse;

/**
 * @author GJW
 * @time: 2020/12/23 18:57
 */
public abstract class MyException extends RuntimeException{

    private Integer code;
    private String message;

    public MyException(ResultStatus resultStatus){
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
    }

}
