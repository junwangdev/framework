package org.gudian.web.exception;

import lombok.Getter;
import org.gudian.web.result.ResultStatus;

/**
 * @author GJW
 * : 2020/12/23 18:57
 */
@Getter
public class MyException extends RuntimeException{

    private ResultStatus resultStatus;

    public MyException(ResultStatus resultStatus){
        this.resultStatus = resultStatus;
    }

}
