package org.gudian.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author GJW
 * SpringMVC的配置
 * @time: 2020/12/23 9:26
 */
@Configuration
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

