package com.lyj.blog.config;

import com.lyj.blog.model.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

/**
 * 用于覆盖配置中的配置（配置从数据库加载）
 */
@Slf4j
public class CoverPropertiesListener implements SpringApplicationRunListener {
    public CoverPropertiesListener(SpringApplication application, String[] args) {
        log.info("加载配置文件覆盖监听器");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        Config config = loadConfigFromDB(environment);
        if(config==null){
            log.error("从数据库加载配置文件失败");
            return;
        }

        String[] activeProfiles = environment.getActiveProfiles();
        // 如果是生产环境,则添加覆盖配置
        if(activeProfiles.length==1 && "prod".equals(activeProfiles[0])){
            MutablePropertySources propertySources = environment.getPropertySources();
            HashMap<String, String> propertyMap = new HashMap<>();
            propertyMap.put("spring.redis.password",config.getRedisPassword());//从数据库加载
            propertySources.addFirst(new OriginTrackedMapPropertySource("coverPropertiesMap",propertyMap));
        }
        log.info("配置文件覆盖成功");
    }

    // 从数据库加载配置
    private Config loadConfigFromDB(ConfigurableEnvironment environment){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//加载驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        // 从配置文件夹加载jdbc的配置
        Properties jdbcProperty = getJdbcProperty(environment);
        if(jdbcProperty==null){
            log.error("数据库配置参数缺失，请检查");
            return null;
        }

    // 从数据库获取配置
    Config config=null;
    try(Connection connection = DriverManager.getConnection(
            (String)jdbcProperty.get("url"),
            (String)jdbcProperty.get("username"),
            (String)jdbcProperty.get("password"))){
        // 查询数据
        ResultSet resultSet = connection
                .prepareStatement("select * from config where id = 1").executeQuery();
        if(resultSet.next() && resultSet.getRow()==1){
            config=new Config();
            config.setSecretId(resultSet.getString("secret_id"));
            config.setSecretKey(resultSet.getString("secret_key"));
            config.setEmail(resultSet.getString("email"));
            config.setEmailPassword(resultSet.getString("email_password"));
            config.setRedisPassword(resultSet.getString("redis_password"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return config;
    }

    // 根据优先级加载配置文件中关于jdbc的配置
    private Properties getJdbcProperty(ConfigurableEnvironment environment){
        Properties properties = new Properties();
        properties.setProperty("url","");
        properties.setProperty("username","");
        properties.setProperty("password","");

        MutablePropertySources propertySources = environment.getPropertySources();
        for (PropertySource<?> next : propertySources) {
            if ("".equals(properties.get("url")) && next.containsProperty("spring.datasource.url")) {
                properties.setProperty("url", (String) next.getProperty("spring.datasource.url"));
            }
            if ("".equals(properties.get("username")) && next.containsProperty("spring.datasource.username")) {
                properties.setProperty("username", (String) next.getProperty("spring.datasource.username"));
            }
            if ("".equals(properties.get("password")) && next.containsProperty("spring.datasource.password")) {
                properties.setProperty("password", String.valueOf(next.getProperty("spring.datasource.password")));
            }
        }
        if("".equals(properties.get("url")) || "".equals(properties.get("username")) || "".equals(properties.get("password"))){
            return null;
        }
        return properties;
    }
}
