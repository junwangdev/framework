package org.gudian.security.filter;

import org.gudian.security.JwtTokenUtil;
import org.gudian.security.SecurityConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author GJW
 * JWT登录授权过滤器
 * 2020/12/22 16:41
 */

@ConditionalOnBean(UserDetailsService.class)
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    /**
     * 必须实现此接口 用户登陆时通过用户名获取用户信息
     * */

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SecurityConfigProperties securityConfigProperties;

    @Autowired
    public PathMatcher pathMatcher;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        //判断是否是匿名访问的接口 如果是直接放行，不进行token解析
        List<String> ignoreUrls = securityConfigProperties.getIgnoreUrls();

        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl,request.getRequestURI())) {
                //这里放行后直接调用controller
                chain.doFilter(request,response);
                return;
            }
        }

        String tokenName = securityConfigProperties.getTokenHeader();
        //获取请求头
        String authToken = request.getHeader(tokenName);

        if (authToken != null) {

            //获取当前登陆用户名称
            String username = jwtTokenUtil.getUserNameFromToken(authToken);


            //当前用户名不为空 并且是未登录状态
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                //通过用户名查询用户
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                //验证Token
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    //创建凭证
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //放入凭证
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}