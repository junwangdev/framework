package org.gudian.security;

import org.gudian.security.filter.JwtAuthenticationTokenFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author GJW
 * 2020/12/23 10:35
 */
@Configuration
@EnableConfigurationProperties(SecurityConfigProperties.class)
@ComponentScan(value = "org.gudian.security")
public class SecurityConfiguration {

    /**
     * 设置密码加密规则
     * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
