package com.lyj.blog.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.HashMap;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/15 4:04 下午
 */
@Configuration
@EnableCaching(proxyTargetClass = true) //开启注解缓存,并使用CGLIB代理
public class RedisConfig {

    //返回cacheManager,并配置好key的过期时间
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 设置特殊的key过期时间
        HashMap<String, RedisCacheConfiguration> entryTtlMap = new HashMap<>();
        entryTtlMap.put("VisitCount",creatRedisCacheConfiguration(Duration.ZERO));//将VisitCount设置为不过期
        entryTtlMap.put("VisitCountAll",creatRedisCacheConfiguration(Duration.ofHours(1)));//将VisitCountAll设置为1小时过期

        // 创建缓存注解管理对立
        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                creatRedisCacheConfiguration(Duration.ofMinutes(15)), //默认缓存配置(过期时间15分钟)
                entryTtlMap //特殊key的过期配置
        );
    }

    // 创建redis缓存配置
    public RedisCacheConfiguration creatRedisCacheConfiguration(@NotNull Duration duration){
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(duration) // 设置key的过期时间
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json())) //设置value的序列化器
                .disableCachingNullValues(); //关闭缓存null值
    }

}
