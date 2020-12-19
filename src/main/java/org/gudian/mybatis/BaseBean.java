package org.gudian.mybatis;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author GJW
 * 基础bean，所有bean都要继承它
 *
 * @time: 2020/12/17 15:16
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseBean {

    /**
     * 表主键
     * */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建日期
     * */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改日期
     * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;


    /**
     * 逻辑删除
     * */
    @TableLogic(value = "0",delval = "1")
    private int deleted;

}
