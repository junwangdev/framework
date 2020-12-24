package org.gudian.web.swagger;

import cn.hutool.core.util.StrUtil;
import org.gudian.security.SecurityConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GJW
 * swagger 配置
 * @time: 2020/12/24 10:28
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerConfigProperites.class)
public class SwaggerConfiguration {

    @Autowired
    private SwaggerConfigProperites swaggerConfigProperites;

    @Bean
    public Docket createRestApi() {
        String basePackage = StrUtil.blankToDefault(swaggerConfigProperites.getBasePackage(),"org.gudian.controller");

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("汇总")
                .apiInfo( apiInfo() )
                .select()
                //扫描指定包下面的controller
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }



    public ApiInfo apiInfo(){
        String title = StrUtil.blankToDefault(swaggerConfigProperites.getTitle(),"后端接口文档");
        String description = StrUtil.blankToDefault(swaggerConfigProperites.getDescription(),"描述后端接口详细方式的接口文档");
        String version = StrUtil.blankToDefault(swaggerConfigProperites.getVersion(),"0.1");
        String license = StrUtil.blankToDefault(swaggerConfigProperites.getLicense(),"版权所有：古典");
        String licenseUrl = StrUtil.blankToDefault(swaggerConfigProperites.getLicenseUrl(),"");
        String contact = StrUtil.blankToDefault(swaggerConfigProperites.getContactName(),"古典");
        String contactUrl = StrUtil.blankToDefault(swaggerConfigProperites.getContactUrl(),"https://blog.csdn.net/dndndnnffj");
        String contactEmail = StrUtil.blankToDefault(swaggerConfigProperites.getContactEmail(),"");

        return  new ApiInfoBuilder()
                .title(  title )
                .description( description )
                .version( version )
                .contact( new Contact( contact,contactUrl,contactEmail ) )
                .license( license )
                .licenseUrl( licenseUrl )
                .build();
    }
}
