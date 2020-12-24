package org.gudian.security.dynamic;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * @author GJW
 * 如需要动态权限校验，需要实现该接口
 * key为接口路径，如 /user/*
 * value为该接口需要的权限 如 user:delete
 * @time: 2020/12/24 21:57
 */

public interface DynamicSecurityService {

    Map<String, ConfigAttribute> loadDataSource();

}
