package org.gudian.mybatis.druid;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置Druid的监控
 * @author GJW
 *  2020/12/26 21:08
 */
@EnableConfigurationProperties(DruidConfigurationProperties.class)
@Configuration
public class DruidConfig {


    @Autowired
    private DruidConfigurationProperties configProperties;


    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
        return  new DruidDataSource();
    }

    //

    /**
     * 配置一个管理后台的Servlet
     * */
    @Bean
    public ServletRegistrationBean statViewServlet(){
        //注册Servlet
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

        Map<String,String> initParams = new HashMap<>();

        //设置登陆用户名
        initParams.put("loginUsername",configProperties.getUsername());
        //设置登陆密码
        initParams.put("loginPassword",configProperties.getPassword());


        //设置ip白名单
        if( CollUtil.isNotEmpty( configProperties.getAllowIp() ) ){

            String allowIp = String.join(",",configProperties.getAllowIp());

            //如果不设置则允许任何人访问
            initParams.put("allow",allowIp);
            //initParams.put("allow", "localhost")：表示只有本机可以访问
        }


        if( CollUtil.isNotEmpty( configProperties.getDenyIp() ) ){

            String denyIp = String.join(",",configProperties.getDenyIp());

            //设置黑名单，不允许指定ip访问
            initParams.put("deny",denyIp);
        }

        bean.setInitParameters(initParams);

        return bean;
    }


    /**
     * 配置web监控的filter
     * */
    @Bean
    public FilterRegistrationBean webStatFilter(){


        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        //排除对静态资源的监控
        Map<String,String> initParams = new HashMap<>();
        initParams.put("exclusions","*.js,*.css,/druid/*");

        bean.setInitParameters(initParams);

        // /* 表示过滤所有请求
        bean.setUrlPatterns(Arrays.asList("/*"));

        return  bean;
    }

}
