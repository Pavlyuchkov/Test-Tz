package com.example.numbergenerateservice.controller;

import com.example.numbergenerateservice.service.OrderNumberGeneratorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderNumberControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderNumberGeneratorService orderNumberGeneratorService;

    @InjectMocks
    private OrderNumberController orderNumberController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderNumberController).build();
        new ObjectMapper();
    }

    @Test
    void generateNumbers_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/generate/numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .string("Батч номеров успешно сгенерирован и отправлен на order-service!"));

        verify(orderNumberGeneratorService, times(1)).createAndSaveBatch();
    }
}