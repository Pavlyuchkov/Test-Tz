package com.example.ordersservice.controller;

import com.example.ordersservice.service.GeneratedNumbersPullService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GeneratedNumbersControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GeneratedNumbersPullService generatedNumbersPullService;

    @InjectMocks
    private GeneratedNumbersController generatedNumbersController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(generatedNumbersController).build();
        new ObjectMapper();
    }

    @Test
    void receiveOrderNumbers() throws Exception {
        List<String> orderNumbers = Arrays.asList("ORD001", "ORD002", "ORD003");

        mockMvc.perform(post("/numbers/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"ORD001\", \"ORD002\", \"ORD003\"]"))
                .andExpect(status().isOk());

        verify(generatedNumbersPullService, times(1)).addOrderNumbers(orderNumbers);
    }
}