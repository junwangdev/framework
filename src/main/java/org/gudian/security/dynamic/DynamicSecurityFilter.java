package org.gudian.security.dynamic;

import org.gudian.security.SecurityConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 动态权限数据源，用于获取当前访问接口需要的权限
 * @author GJW
 * : 2020/12/22 11:32
 */
@Configuration
@ConditionalOnBean(DynamicSecurityService.class)
public class DynamicSecurityFilter extends AbstractSecurityInterceptor implements Filter {


    @Autowired
    private DynamicSecurityMetadataSource dynamicSecurityMetadataSource;


    @Autowired
    private SecurityConfigProperties configProperties;


    @Autowired
    public void setMyAccessDecisionManager(DynamicAccessDecisionManager decisionManager) {
        super.setAccessDecisionManager(decisionManager);
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);

        //OPTIONS请求直接放行
        if(request.getMethod().equals(HttpMethod.OPTIONS.toString())){
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            return;
        }

        //白名单请求直接放行
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String path : configProperties.getIgnoreUrls()) {
            if(pathMatcher.match(path,request.getRequestURI())){
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
                return;
            }
        }

        //此处会调用AccessDecisionManager中的decide方法进行鉴权操作
        InterceptorStatusToken token = super.beforeInvocation(fi);

        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }


    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return dynamicSecurityMetadataSource;
    }

}
