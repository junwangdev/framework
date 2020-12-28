package org.gudian.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gudian.mybatis.BaseBean;

import java.util.Date;

/**
 * @author GJW
 * @time: 2020/12/28 17:12
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SystemLogDto extends BaseBean {

    /**
     * 访问的类名称
     * */
    private String className;

    /**
     * 访问的方法名
     * */
    private String methodName;

    /**
     * 操作时间
     * */
    private Date operationTime;

    /**
     * 调用花费时间（毫秒）
     * */
    private Long time;

    /**
     * 操作类型，主要是增加，查询，删除和修改
     * */
    private OperationType operationType;

    /**
     * 操作所影响的数据类型
     * */
    private String description;

    /**
     * 是否操作成功
     * */
    private Boolean success;


    /**
     * 客户端访问的IP地址
     * */
    private String ipAddress;

    /**
     * 调用时的参数
     * */
    private String parameter;

    /**
     * 调用结果
     * */
    private String result;

}
