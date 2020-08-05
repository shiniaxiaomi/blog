package com.lyj.blog.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.stereotype.Component;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/3 2:42 下午
 */
@Configuration
public class ElasticSearchConfig {

    @Value("${elasticsearch.hostAndPort}")
    private String hostAndPort;

    //配置client客户端
    @Bean
    RestHighLevelClient client() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(hostAndPort)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

}
