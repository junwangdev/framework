package org.gudian.web;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author GJW
 * SpringMVC的配置
 * : 2020/12/23 9:26
 */
@Configuration
@ComponentScan("org.gudian.web")
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("POST", "PUT", "GET", "DELETE", "OPTIONS")
                .allowedOrigins("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}

