package org.gudian.mybatis;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author GJW
 * @time: 2020/12/19 17:33
 */
@Configuration
public class PageHelperConfig {

    public PageHelper getPageHelper(){
        PageHelper pageHelper=new PageHelper();

        Properties properties=new Properties();
        properties.setProperty("helperDialect","mysql");
        properties.setProperty("reasonable","false");
        properties.setProperty("supportMethodsArguments","true");
        properties.setProperty("params","count=countSql");

        pageHelper.setProperties(properties);
        return pageHelper;
    }

}
