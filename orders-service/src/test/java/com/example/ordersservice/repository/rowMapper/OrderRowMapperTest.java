package com.example.ordersservice.repository.rowMapper;

import com.example.ordersservice.enums.DeliveryType;
import com.example.ordersservice.enums.PaymentType;
import com.example.ordersservice.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderRowMapperTest {

    private RowMapper<Order> orderRowMapper;

    @BeforeEach
    void setUp() {
        orderRowMapper = new OrderRowMapper();
    }

    @Test
    void mapRow_mapOrderSuccess() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("order_number")).thenReturn("ORD1001");
        when(rs.getBigDecimal("total_amount")).thenReturn(BigDecimal.valueOf(1000));
        when(rs.getObject("order_date", LocalDate.class)).thenReturn(LocalDate.of(2024, 12, 2));
        when(rs.getString("recipient")).thenReturn("Иван Иванов");
        when(rs.getString("delivery_address")).thenReturn("Москва, ул. Ленина");
        when(rs.getString("payment_type")).thenReturn("CARD");
        when(rs.getString("delivery_type")).thenReturn("PICKUP");

        Order order = orderRowMapper.mapRow(rs, 1);

        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals("ORD1001", order.getOrderNumber());
        assertEquals(BigDecimal.valueOf(1000), order.getTotalAmount());
        assertEquals(LocalDate.of(2024, 12, 2), order.getOrderDate());
        assertEquals("Иван Иванов", order.getRecipient());
        assertEquals("Москва, ул. Ленина", order.getDeliveryAddress());
        assertEquals(PaymentType.CARD, order.getPaymentType());
        assertEquals(DeliveryType.PICKUP, order.getDeliveryType());
    }

    @Test
    void mapRow_invalidPaymentType() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString("payment_type")).thenReturn("INVALID_PAYMENT_TYPE");

        SQLException exception = assertThrows(SQLException.class, () -> orderRowMapper.mapRow(rs, 1));
        assertTrue(exception.getMessage().contains("Неизвестный тип оплаты"));
    }

    @Test
    void mapRow_handleNullPayment() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString("payment_type")).thenReturn(null);

        SQLException exception = assertThrows(SQLException.class, () -> orderRowMapper.mapRow(rs, 1));
        assertTrue(exception.getMessage().contains("Неизвестный тип оплаты"));
    }
}
