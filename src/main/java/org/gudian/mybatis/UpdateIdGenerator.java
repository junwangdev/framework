package org.gudian.mybatis;

/**
 * @author GJW
 * 生成updateId接口
 * 用于再修改表时设置表的修改id(如修改人id)
 * 该接口应被实现
 * @time: 2020/12/18 14:03
 */
public interface UpdateIdGenerator {
    /**
     *  返回 updateId
     * @return 返回每次修改表时的 updateId 用于自动填充
     * */
    Long generator();
}
