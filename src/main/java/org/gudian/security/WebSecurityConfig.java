package org.gudian.security;

import lombok.extern.slf4j.Slf4j;
import org.gudian.security.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * @author GJW
 * : 2020/12/22 16:35
 */

@Configuration
@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@ConditionalOnBean(UserDetailsService.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 必须实现此接口，用于通过用户名获取当前登陆账号状态
     * */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     * 当访问接口没有权限时，如要自定义的返回结果，应实现 AccessDeniedHandler  接口
     * */
    @Autowired(required = false)
    private AccessDeniedHandler accessDeniedHandler;

    /**
     * 当未登录或者token失效访问接口时，如要自定义的返回结果，应实现 AuthenticationEntryPoint 接口
     * */
    @Autowired(required = false)
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private SecurityConfigProperties securityConfigProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        //允许不用登陆即可访问的url
        List<String> ignoreUrls = securityConfigProperties.getIgnoreUrls();


        log.info("允许匿名访问的接口==>"+ignoreUrls);

        httpSecurity.csrf()// 由于使用的是JWT，我们这里不需要csrf
                .disable()
                .sessionManagement()// 基于token，所以不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 允许对于网站静态资源的无授权访问
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                )
                .permitAll()
                //将配置的路径设置为无权即可访问
                .antMatchers(ignoreUrls.toArray(new String[0]))
                .permitAll()
                //跨域请求会先进行一次options请求
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .authenticated();
        // 禁用缓存
        httpSecurity.headers().cacheControl();

        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);


        if(accessDeniedHandler!=null && authenticationEntryPoint !=null){
            //添加自定义未授权和未登录结果返回
            httpSecurity.exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler)
                    .authenticationEntryPoint(authenticationEntryPoint);
        }


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
