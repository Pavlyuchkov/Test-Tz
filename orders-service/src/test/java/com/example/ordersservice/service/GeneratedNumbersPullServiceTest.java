package com.example.ordersservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GeneratedNumbersPullServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeneratedNumbersPullService generatedNumbersPullService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addOrderNumbers() {
        List<String> orderNumbers = List.of("ORD1001", "ORD1002", "ORD1003");

        generatedNumbersPullService.addOrderNumbers(orderNumbers);

        String orderNumber = generatedNumbersPullService.getOrderNumber();
        assertEquals("ORD1001", orderNumber);
    }

    @Test
    void getOrderNumber() {
        List<String> orderNumbers = List.of("ORD1001", "ORD1002", "ORD1003");
        generatedNumbersPullService.addOrderNumbers(orderNumbers);

        String orderNumber = generatedNumbersPullService.getOrderNumber();

        assertEquals("ORD1001", orderNumber);
    }
}