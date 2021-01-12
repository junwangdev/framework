package org.gudian.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author GJW
 * SpringMVC的配置
 * : 2020/12/23 9:26
 */
@Configuration
@ComponentScan("org.gudian.web")
public class WebConfig implements WebMvcConfigurer {


    /**
     * 跨域配置
     * */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("POST", "PUT", "GET", "DELETE", "OPTIONS")
                .allowedOrigins("*")
                .allowCredentials(false)
                .maxAge(3600);
    }


    /**
     * 配置 Jackson 序列化
     * */
    @Bean
     public ObjectMapper objectMapper(){

        ObjectMapper mapper = new ObjectMapper();

        //设置返回json时忽略null值
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置日期类型格式
        mapper.setDateFormat(sdf);

        return mapper;
    }
}

