package com.example.numbergenerateservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OrderNumberRepositoryTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private OrderNumberRepository orderNumberRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIncrementCounterAndResetDate() {
        Long expectedResult = 1L;
        when(redisTemplate.execute(any(DefaultRedisScript.class), anyList(), anyString()))
                .thenReturn(expectedResult);
        Long result = orderNumberRepository.incrementCounterAndResetDate("counterKey", "lastResetDateKey", "2024-12-03");

        verify(redisTemplate, times(1)).execute(any(DefaultRedisScript.class), anyList(), anyString());

        assertEquals(expectedResult, result);
    }

    @Test
    void testSaveNumbersBatch() {
        List<String> orderNumbers = List.of("00001", "00002", "00003");
        ListOperations<String, String> listOperations = mock(ListOperations.class);

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.rightPushAll(eq("ORDER_NUMBER_BATCH_KEY"), eq(orderNumbers))).thenReturn((long) orderNumbers.size());

        orderNumberRepository.saveNumbersBatch("ORDER_NUMBER_BATCH_KEY", orderNumbers);

        verify(listOperations, times(1)).rightPushAll(eq("ORDER_NUMBER_BATCH_KEY"), eq(orderNumbers));
    }

    @Test
    void testSetBatchTTL() {
        Duration ttl = Duration.ofMinutes(120);
        when(redisTemplate.expire(eq("ORDER_NUMBER_BATCH_KEY"), eq(ttl))).thenReturn(true);

        orderNumberRepository.setBatchTTL("ORDER_NUMBER_BATCH_KEY", 120);

        verify(redisTemplate, times(1)).expire(eq("ORDER_NUMBER_BATCH_KEY"), eq(ttl));
    }

    @Test
    void testGetBatchFromRedis() {
        ListOperations<String, String> listOps = mock(ListOperations.class);
        when(redisTemplate.opsForList()).thenReturn(listOps);

        List<String> mockOrderNumbers = List.of("00001", "00002", "00003");
        when(listOps.range(eq("ORDER_NUMBER_BATCH_KEY"), eq(0L), eq(-1L)))
                .thenReturn(mockOrderNumbers);

        List<String> result = orderNumberRepository.getBatchFromRedis("ORDER_NUMBER_BATCH_KEY");

        verify(redisTemplate, times(1)).opsForList();
        verify(listOps, times(1)).range(eq("ORDER_NUMBER_BATCH_KEY"), eq(0L), eq(-1L));

        assertEquals(mockOrderNumbers, result);
    }
}
