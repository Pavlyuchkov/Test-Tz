package com.example.ordersservice.repository;

import com.example.ordersservice.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    void createOrder(Order order);

    Optional<Order> getOrderById(Long id);

    List<Order> getOrdersByDateAndAmount(LocalDate date, BigDecimal minAmount);

    List<Order> getOrdersExcludingProduct(Long productCode, LocalDate startDate, LocalDate endDate);
}