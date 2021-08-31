package com.lyj.blog.service;

import com.lyj.blog.mapper.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisService {

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    // 缓存访问总数（过期时间1小时）
    @Cacheable("VisitCountAll")
    public Integer selectVisitCountAll() {
        Integer count = blogMapper.selectSum();
        return count == null ? 0 : count;
    }

    // 手动自增blog对应的访问次数(先看key存不存在，如果不存在，则设置成数据库当中的值)
    public void incrVisitCountByBlogId(int blogId) {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            Boolean exists = connection.exists(("VisitCount::" + blogId).getBytes());
            if (exists == null || !exists) {
                // 去数据库查询对应的访问总数
                Integer visitCount = blogMapper.getVisitCountByBlogId(blogId);
                connection.set(("VisitCount::" + blogId).getBytes(), String.valueOf(visitCount).getBytes());
            } else {
                connection.incr(("VisitCount::" + blogId).getBytes());
            }
        }
    }

    // 每天0点写一次库
    @Scheduled(cron = "0 0 0 * * *")
    public void writeVisitCountToDB() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            Set<byte[]> keys = connection.keys("VisitCount::*".getBytes());
            if (keys == null) {
                return;
            }
            for (byte[] bytes : keys) {
                byte[] keyBytes = connection.get(bytes);
                if (keyBytes == null) {
                    continue;
                }
                Integer visitCount = Integer.valueOf(new String(keyBytes));
                Integer blogId = Integer.valueOf(new String(bytes).substring("VisitCount::".length()));
                blogMapper.updateVisitCountByBlogId(blogId, visitCount);
            }
        }
    }

}
