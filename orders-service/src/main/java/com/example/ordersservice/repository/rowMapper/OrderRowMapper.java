package com.example.ordersservice.repository.rowMapper;

import com.example.ordersservice.enums.DeliveryType;
import com.example.ordersservice.enums.PaymentType;
import com.example.ordersservice.model.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrderRowMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Order.builder()
                .id(rs.getLong("id"))
                .orderNumber(rs.getString("order_number"))
                .totalAmount(rs.getBigDecimal("total_amount"))
                .orderDate(rs.getObject("order_date", LocalDate.class))
                .recipient(rs.getString("recipient"))
                .deliveryAddress(rs.getString("delivery_address"))
                .paymentType(convertToPaymentType(rs.getString("payment_type")))
                .deliveryType(convertToDeliveryType(rs.getString("delivery_type")))
                .build();
    }

    private PaymentType convertToPaymentType(String paymentTypeStr) throws SQLException {
        try {
            return PaymentType.valueOf(paymentTypeStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SQLException("Неизвестный тип оплаты: " + paymentTypeStr, e);
        }
    }

    private DeliveryType convertToDeliveryType(String deliveryTypeStr) throws SQLException {
        try {
            return DeliveryType.valueOf(deliveryTypeStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SQLException("Неизвестный тип доставки: " + deliveryTypeStr, e);
        }
    }
}