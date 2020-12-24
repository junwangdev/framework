package org.gudian.security.dynamic;

import cn.hutool.core.util.URLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;
import java.util.*;


/**
 * 动态权限数据源，用于获取当前访问接口需要的权限
 * @author GJW
 * : 2020/12/22 11:32
 */
@Configuration
@ConditionalOnBean(DynamicSecurityService.class)
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static Map<String, ConfigAttribute> configAttributeMap = null;

    @Autowired
    private DynamicSecurityService dynamicSecurityService;

    @PostConstruct
    public void loadDataSource() {
        configAttributeMap = dynamicSecurityService.loadDataSource();
    }

    /**
     * 清空权限缓存
     * */
    public void clearDataSource() {
        configAttributeMap.clear();
        configAttributeMap = null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        //如果权限缓存为空，加载数据
        if (configAttributeMap == null) this.loadDataSource();

        List<ConfigAttribute>  configAttributes = new ArrayList<>();

        //获取当前访问的路径
        String url = ((FilterInvocation) o).getRequestUrl();

        String path = URLUtil.getPath(url);

        PathMatcher pathMatcher = new AntPathMatcher();

        Iterator<String> iterator = configAttributeMap.keySet().iterator();

        //获取访问该路径所需资源
        while (iterator.hasNext()) {
            String pattern = iterator.next();
            if (pathMatcher.match(pattern, path)) {
                //放入该接口需要的权限
                configAttributes.add(configAttributeMap.get(pattern));
            }
        }


        // 将当前访问的接口所需要的权限返回
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
