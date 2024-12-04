package com.example.ordersservice.entity;

import com.example.ordersservice.model.OrderDetail;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDetailTest {

    @Test
    void testOrderDetailFields() {
        OrderDetail orderDetail = OrderDetail.builder()
                .id(1L)
                .productCode(12345L)
                .productName("Товар 1")
                .quantity(10L)
                .unitPrice(BigDecimal.valueOf(99.99))
                .orderId(100L)
                .build();

        assertEquals(1L, orderDetail.getId());
        assertEquals(12345L, orderDetail.getProductCode());
        assertEquals("Товар 1", orderDetail.getProductName());
        assertEquals(10L, orderDetail.getQuantity());
        assertEquals(BigDecimal.valueOf(99.99), orderDetail.getUnitPrice());
        assertEquals(100L, orderDetail.getOrderId());
    }
}