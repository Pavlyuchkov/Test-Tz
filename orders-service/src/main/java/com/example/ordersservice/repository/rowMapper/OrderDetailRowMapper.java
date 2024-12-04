package com.example.ordersservice.repository.rowMapper;

import com.example.ordersservice.model.OrderDetail;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDetailRowMapper implements RowMapper<OrderDetail> {

    @Override
    public OrderDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
        return OrderDetail.builder()
                .productCode(rs.getLong("product_code"))
                .productName(rs.getString("product_name"))
                .quantity(rs.getLong("quantity"))
                .unitPrice(rs.getBigDecimal("unit_price"))
                .build();
    }
}