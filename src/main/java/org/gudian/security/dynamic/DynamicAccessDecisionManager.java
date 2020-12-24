package org.gudian.security.dynamic;

import cn.hutool.core.collection.CollUtil;
import org.gudian.web.exception.MyException;
import org.gudian.web.result.ResultStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * 动态权限数据源，用于获取当前访问接口需要的权限
 * @author GJW
 * : 2020/12/22 11:32
 */
@Configuration
@ConditionalOnBean(DynamicSecurityService.class)
public class DynamicAccessDecisionManager implements AccessDecisionManager {


    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) {

        // 当接口未被配置资源时直接放行
        if (CollUtil.isEmpty(configAttributes)) {
            return;
        }

        Iterator<ConfigAttribute> iterator = configAttributes.iterator();


        //循环判断是否拥有权限
        while (iterator.hasNext()) {

            ConfigAttribute configAttribute = iterator.next();
            //将访问所需资源或用户拥有资源进行比对
            String needAuthority = configAttribute.getAttribute();

            //authentication.getAuthorities() 是用户当前所拥有的权限
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {

                if (needAuthority.trim().equals(grantedAuthority.getAuthority())) {
                    return;
                }
            }
        }
        //如果该用户没有权限，抛异常 必须抛该异常走AccessDeniedHandler
        throw new AccessDeniedException("抱歉，您没有访问权限");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
