package org.gudian.mybatis.druid;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author GJW
 *  2020/12/26 21:13
 */
@Data
@ConfigurationProperties("druid.config")
public class DruidConfigurationProperties {

    //web页面ip
    private String username = "admin";

    //密码
    private String password = "admin123";

    //ip白名单
    private List<String> allowIp;

    //ip黑名单
    private List<String> denyIp;

}
