package com.example.numbergenerateservice.service;

import com.example.numbergenerateservice.repository.OrderNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderNumberBatchSenderService {

    @Value("${orders.service.url}")
    private String orderServiceUrl;

    private final RestTemplate restTemplate;
    private final OrderNumberRepository orderNumberRepository;

    public static final String ORDER_NUMBER_BATCH_KEY = "order_number_batch";

    public void sendBatchOfOrderNumbers() {
        List<String> batch = orderNumberRepository.getBatchFromRedis(ORDER_NUMBER_BATCH_KEY);
        if (!batch.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<String>> request = new HttpEntity<>(batch, headers);

            restTemplate.postForEntity(orderServiceUrl, request, Void.class);
        }
    }
}