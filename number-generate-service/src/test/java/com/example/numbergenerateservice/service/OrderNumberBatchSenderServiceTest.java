package com.example.numbergenerateservice.service;

import com.example.numbergenerateservice.repository.OrderNumberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderNumberBatchSenderServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OrderNumberRepository orderNumberRepository;

    @InjectMocks
    private OrderNumberBatchSenderService orderNumberBatchSenderService;

    @Test
    void sendBatchOfOrderNumbers_WhenBatchIsNotEmpty_ShouldSendPostRequest() {
        String mockOrderServiceUrl = "http://test-tz/prilaga";
        ReflectionTestUtils.setField(orderNumberBatchSenderService, "orderServiceUrl", mockOrderServiceUrl);

        List<String> mockBatch = List.of("202400001", "202400002", "202400003");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> expectedRequest = new HttpEntity<>(mockBatch, headers);

        when(orderNumberRepository.getBatchFromRedis(OrderNumberBatchSenderService.ORDER_NUMBER_BATCH_KEY))
                .thenReturn(mockBatch);

        orderNumberBatchSenderService.sendBatchOfOrderNumbers();

        verify(restTemplate, times(1))
                .postForEntity(mockOrderServiceUrl, expectedRequest, Void.class);
        verify(orderNumberRepository, times(1))
                .getBatchFromRedis(OrderNumberBatchSenderService.ORDER_NUMBER_BATCH_KEY);
    }

    @Test
    void sendBatchOfOrderNumbers_WhenBatchIsEmpty_ShouldNotSendPostRequest() {
        when(orderNumberRepository.getBatchFromRedis(OrderNumberBatchSenderService.ORDER_NUMBER_BATCH_KEY))
                .thenReturn(List.of());

        orderNumberBatchSenderService.sendBatchOfOrderNumbers();

        verify(restTemplate, never())
                .postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
        verify(orderNumberRepository, times(1))
                .getBatchFromRedis(OrderNumberBatchSenderService.ORDER_NUMBER_BATCH_KEY);
    }
}
