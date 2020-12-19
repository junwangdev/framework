package org.gudian.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @author GJW
 * MybatisPlus 自动填充功能实现
 * @time: 2020/12/18 13:55
 */
@Configuration
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
    *  注入updateId 自动生成器
    * */
    @Autowired(required = false)
    private UpdateIdGenerator updateIdGenerator;

    /**
     * 插入时执行
     * */
    @Override
    public void insertFill(MetaObject metaObject) {

        //设置创建时间
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());

        //设置修改时间
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());

        //如果实现了 updateIdGenerator接口，则调用generator生成updateId
        if( updateIdGenerator !=null ){
            Long updateId = updateIdGenerator.generator();
            this.strictInsertFill(metaObject, "updateId", Long.class, updateId);
        }

    }


    /**
     * 修改时执行
     * */
    @Override
    public void updateFill(MetaObject metaObject) {

        //设置修改时间
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());

        //如果实现了 updateIdGenerator接口，则调用generator生成updateId
        if( updateIdGenerator !=null ){
            Long updateId = updateIdGenerator.generator();
            this.strictInsertFill(metaObject, "updateId", Long.class, updateId);
        }
    }

}
