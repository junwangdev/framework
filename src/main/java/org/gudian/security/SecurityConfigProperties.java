package org.gudian.security;

import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author GJW
 * @time: 2020/12/22 11:32
 */
@Component
@ConfigurationProperties(prefix = "security")
@Getter
@ToString
public class SecurityConfigProperties {


    /**
     * token请求头名称
     * */
    private String tokenHeader;

    /**
     * token加解密使用的密钥
     * */
    private String tokenSecret;

    /**
     * token过期时间（秒）
     * */
    private Long tokenExpiration;


    /**
     * 访问路径白名单，无需登陆即可访问
     * */
    private List<String> ignoreUrls;


}
