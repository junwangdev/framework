package org.gudian.security.filter;

import org.gudian.security.JwtTokenUtil;
import org.gudian.security.SecurityConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author GJW
 * JWT登录授权过滤器
 * 2020/12/22 16:41
 */

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    /**
     * 必须实现此接口 用户登陆时通过用户名获取用户信息
     * */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SecurityConfigProperties securityConfigProperties;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

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