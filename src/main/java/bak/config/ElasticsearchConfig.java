package bak.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

/**
 * 配置Elasticsearch
 * @author yingjie.lu
 * @version 1.0
 * @date 2019/12/4 10:17 上午
 */

//@Configuration
public class ElasticsearchConfig {

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
