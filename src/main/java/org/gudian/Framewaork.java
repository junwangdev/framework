package org.gudian;

import io.swagger.annotations.Api;
import org.gudian.security.SecurityConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author GJW
 * : 2020/12/19 18:08
 */
@SpringBootApplication
public class Framewaork {


  /*  public static void main(String[] args) {

        SpringApplication.run(Framewaork.class);
    }*/

    public static void main(String[] args) {
        String a = "${DATAMONTH}";
        System.out.println(a.replaceAll("\\$\\{DATAMONTH\\}","123"));
    }
}
