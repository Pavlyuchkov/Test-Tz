package com.example.ordersservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneratedNumbersPullService {

    @Value("${number-generate-service.url}")
    private String numberGenerateServiceUrl;

    private final RestTemplate restTemplate;
    private final Queue<String> orderNumbersQueue = new ConcurrentLinkedQueue<>();

    public void addOrderNumbers(List<String> orderNumbers) {
        orderNumbersQueue.addAll(orderNumbers);
    }

    public String getOrderNumber() {
        if (orderNumbersQueue.isEmpty()) {
            fetchOrderNumbersFromGeneratorService();
        }
        return orderNumbersQueue.poll();
    }

    private void fetchOrderNumbersFromGeneratorService() {
        ResponseEntity<Void> response = restTemplate.postForEntity(numberGenerateServiceUrl, null, Void.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Номера успешно получены от сервиса.");
        } else {
            log.error("Ошибка при получении номеров: {}", response.getStatusCode());
        }
    }
}