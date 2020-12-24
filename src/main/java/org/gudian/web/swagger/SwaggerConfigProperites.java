package org.gudian.web.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author GJW
 * @time: 2020/12/24 10:34
 */
@Data
@ConfigurationProperties(prefix = "swagger.config")
public class SwaggerConfigProperites {

    /**
     * 要扫描controller的包
     * */
    private String basePackage;

    /**
     * swagger文档标题
     * */
    private String title;

    /**
     * swagger文档地址
     * */
    private String version;

    /**
     * 接口文档描述
     * */
    private String description;

    /**
     * 联系名称（公司名称）
     * */
    private String contactName;

    /**
     * 联系url
     * */
    private String contactUrl;

    /**
     * 邮箱
     * */
    private String contactEmail;


    /**
     * 许可名称
     * */
    private String license;

    /**
     * 许可url
     * */
    private String licenseUrl;

}
