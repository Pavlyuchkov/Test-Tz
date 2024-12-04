package com.example.ordersservice.entity;

import com.example.ordersservice.model.Order;
import com.example.ordersservice.model.OrderDetail;
import org.junit.jupiter.api.Test;
import com.example.ordersservice.enums.PaymentType;
import com.example.ordersservice.enums.DeliveryType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderTest {

    @Test
    void testCalculateTotalAmountWithDetails() {
        Order order = Order.builder()
                .details(List.of(
                        OrderDetail.builder().unitPrice(BigDecimal.valueOf(100)).quantity(2L).build(),
                        OrderDetail.builder().unitPrice(BigDecimal.valueOf(200)).quantity(1L).build()
                ))
                .build();
        order.calculateTotalAmount();
        assertEquals(BigDecimal.valueOf(400), order.getTotalAmount());
    }

    @Test
    void testCalculateTotalAmountWithEmptyDetails() {
        Order order = Order.builder()
                .details(List.of())
                .build();
        order.calculateTotalAmount();
        assertEquals(BigDecimal.ZERO, order.getTotalAmount());
    }

    @Test
    void testCalculateTotalAmountWithNullDetails() {
        Order order = Order.builder()
                .details(null)
                .build();
        order.calculateTotalAmount();
        assertEquals(BigDecimal.ZERO, order.getTotalAmount());
    }

    @Test
    void testOrderFields() {
        Order order = Order.builder()
                .id(1L)
                .orderNumber("testOrder")
                .orderDate(LocalDate.now())
                .recipient("Иван Иванов")
                .deliveryAddress("Москва, Кремль")
                .paymentType(PaymentType.CARD)
                .deliveryType(DeliveryType.PICKUP)
                .build();

        assertEquals(1L, order.getId());
        assertEquals("testOrder", order.getOrderNumber());
        assertNotNull(order.getOrderDate());
        assertEquals("Иван Иванов", order.getRecipient());
        assertEquals("Москва, Кремль", order.getDeliveryAddress());
        assertEquals(PaymentType.CARD, order.getPaymentType());
        assertEquals(DeliveryType.PICKUP, order.getDeliveryType());
    }
}