package org.gudian.log;

/**
 * @author GJW
 * 日志处理类，如需实现日志处理必须实现该接口
 * time: 2021/1/4 16:39
 */
public interface LogHandler {

     void handler(SystemLogDto systemLogDto);
}
