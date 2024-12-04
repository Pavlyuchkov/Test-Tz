package com.example.ordersservice.repository.rowMapper;

import com.example.ordersservice.model.OrderDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderDetailRowMapperTest {

    private OrderDetailRowMapper orderDetailRowMapper;

    @BeforeEach
    void setUp() {
        orderDetailRowMapper = new OrderDetailRowMapper();
    }

    @Test
    void mapRow_mapOrderDetailSuccess() throws SQLException {
        ResultSet rs = mock(ResultSet.class);

        when(rs.getLong("product_code")).thenReturn(123L);
        when(rs.getString("product_name")).thenReturn("Product A");
        when(rs.getLong("quantity")).thenReturn(10L);
        when(rs.getBigDecimal("unit_price")).thenReturn(BigDecimal.valueOf(99.99));

        OrderDetail orderDetail = orderDetailRowMapper.mapRow(rs, 1);

        assertNotNull(orderDetail);
        assertEquals(123L, orderDetail.getProductCode());
        assertEquals("Product A", orderDetail.getProductName());
        assertEquals(10L, orderDetail.getQuantity());
        assertEquals(BigDecimal.valueOf(99.99), orderDetail.getUnitPrice());
    }

    @Test
    void mapRow_throwSQLException() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getLong("product_code")).thenThrow(new SQLException("Failed to retrieve product code"));

        SQLException exception = assertThrows(SQLException.class, () -> orderDetailRowMapper.mapRow(rs, 1));
        assertTrue(exception.getMessage().contains("Failed to retrieve product code"));
    }
}
