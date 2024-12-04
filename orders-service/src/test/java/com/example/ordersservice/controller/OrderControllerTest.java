package com.example.ordersservice.controller;

import com.example.ordersservice.dto.request.OrderRequestDTO;
import com.example.ordersservice.dto.response.OrderResponseDTO;
import com.example.ordersservice.enums.DeliveryType;
import com.example.ordersservice.enums.PaymentType;
import com.example.ordersservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setRecipient("Иван Иванов");
        requestDTO.setDeliveryAddress("Москва");
        requestDTO.setPaymentType(PaymentType.CARD);
        requestDTO.setDeliveryType(DeliveryType.PICKUP);

        String requestJson = objectMapper.writeValueAsString(requestDTO);

        Mockito.doNothing().when(orderService).createOrder(Mockito.any(OrderRequestDTO.class));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Заказ успешно оформлен!"))
                .andExpect(content().encoding("UTF-8"));

        verify(orderService).createOrder(Mockito.any(OrderRequestDTO.class));
    }

    @Test
    void testGetOrderById() throws Exception {
        Long orderId = 1L;

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderNumber("ORD-001");
        responseDTO.setRecipient("Иван Иванов");

        Mockito.when(orderService.getOrderById(orderId)).thenReturn(responseDTO);

        mockMvc.perform(get("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value("ORD-001"))
                .andExpect(jsonPath("$.recipient").value("Иван Иванов"));

        verify(orderService).getOrderById(orderId);
    }

    @Test
    void testGetOrdersByDateAndAmount() throws Exception {
        LocalDate date = LocalDate.now();
        BigDecimal minAmount = BigDecimal.valueOf(100);

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderNumber("ORD-002");
        responseDTO.setRecipient("Мария Петрова");

        Mockito.when(orderService.getOrdersByDateAndAmount(date, minAmount))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/orders/filtered")
                        .param("date", date.toString())
                        .param("minAmount", minAmount.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderNumber").value("ORD-002"))
                .andExpect(jsonPath("$[0].recipient").value("Мария Петрова"));

        verify(orderService).getOrdersByDateAndAmount(date, minAmount);
    }

    @Test
    void testGetOrdersExcludingProduct() throws Exception {
        Long productCode = 123L;
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderNumber("ORD-003");
        responseDTO.setRecipient("Сергей Иванов");

        Mockito.when(orderService.getOrdersExcludingProduct(productCode, startDate, endDate))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/orders/excludes")
                        .param("productCode", productCode.toString())
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderNumber").value("ORD-003"))
                .andExpect(jsonPath("$[0].recipient").value("Сергей Иванов"));

        verify(orderService).getOrdersExcludingProduct(productCode, startDate, endDate);
    }
}