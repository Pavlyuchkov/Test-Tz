package com.example.numbergenerateservice.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
@AllArgsConstructor
public class OrderNumberRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String LUA_INCREMENT_AND_RESET_DATE = """
        local lastResetDate = redis.call('GET', KEYS[2])
        if not lastResetDate or lastResetDate ~= ARGV[1] then
        redis.call('SET', KEYS[1], 0)
        redis.call('SET', KEYS[2], ARGV[1])
        end
        return redis.call('INCR', KEYS[1])
        """;

    public Long incrementCounterAndResetDate(String counterKey, String lastResetDateKey, String datePart) {
        return redisTemplate.execute(
                new DefaultRedisScript<>(LUA_INCREMENT_AND_RESET_DATE, Long.class),
                List.of(counterKey, lastResetDateKey),
                datePart
        );
    }

    public void saveNumbersBatch(String key, List<String> orderNumbers) {
        redisTemplate.opsForList().rightPushAll(key, orderNumbers);
    }

    public List<String> getBatchFromRedis(String batchKey) {
        return redisTemplate.opsForList().range(batchKey, 0, -1);
    }

    public void setBatchTTL(String key, long ttlInMinutes) {
        redisTemplate.expire(key, Duration.ofMinutes(ttlInMinutes));
    }
}