package com.example.ordersservice.repository.impl;

import com.example.ordersservice.exception.ProductNotFoundException;
import com.example.ordersservice.model.Order;
import com.example.ordersservice.model.OrderDetail;
import com.example.ordersservice.repository.OrderRepository;
import com.example.ordersservice.repository.rowMapper.OrderDetailRowMapper;
import com.example.ordersservice.repository.rowMapper.OrderRowMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final OrderRowMapper ORDER_ROW_MAPPER = new OrderRowMapper();
    private static final OrderDetailRowMapper ORDER_DETAIL_ROW_MAPPER = new OrderDetailRowMapper();

    private static final String CREATE_ORDER_SQL = """
        INSERT INTO orders
        (order_number, total_amount, order_date, recipient, delivery_address, payment_type, delivery_type)
        VALUES (?, ?, ?, ?, ?, ?, ?)
       """;

    private static final String CREATE_ORDER_DETAILS_SQL = """
        INSERT INTO order_details
        (product_code, product_name, quantity, unit_price, order_id)
        VALUES (?, ?, ?, ?, ?)
        """;

    private static final String GET_ORDER_BY_ID_SQL = """
        SELECT * FROM orders WHERE id = ?
        """;

    public static final String GET_ORDER_DETAILS_BY_ORDER_ID_SQL = """
        SELECT * FROM order_details WHERE order_id = ?
        """;

    private static final String GET_ORDERS_BY_DATE_AND_AMOUNT_SQL = """
        SELECT * FROM orders WHERE order_date = ? AND total_amount >= ?
        """;

    private static final String GET_ORDERS_EXCLUDING_PRODUCT_SQL = """
        SELECT * FROM orders o
        WHERE o.order_date BETWEEN ? AND ?
        AND NOT EXISTS (
            SELECT 1
            FROM order_details od
            WHERE od.order_id = o.id AND od.product_code = ?
        )
        """;

    @Override
    public void createOrder(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_ORDER_SQL, new String[]{"id"});
            ps.setString(1, order.getOrderNumber());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setObject(3, order.getOrderDate());
            ps.setString(4, order.getRecipient());
            ps.setString(5, order.getDeliveryAddress());
            ps.setString(6, order.getPaymentType().name());
            ps.setString(7, order.getDeliveryType().name());
            return ps;
        }, keyHolder);

        Long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);

        for (OrderDetail detail : order.getDetails()) {
            jdbcTemplate.update(CREATE_ORDER_DETAILS_SQL,
                    detail.getProductCode(),
                    detail.getProductName(),
                    detail.getQuantity(),
                    detail.getUnitPrice(),
                    order.getId());
        }
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        List<Order> orders = jdbcTemplate.query(GET_ORDER_BY_ID_SQL, ORDER_ROW_MAPPER, id);
        if (orders.isEmpty()) {
            return Optional.empty();
        }
        Order order = orders.get(0);
        List<OrderDetail> details = jdbcTemplate.query(GET_ORDER_DETAILS_BY_ORDER_ID_SQL, ORDER_DETAIL_ROW_MAPPER, id);
        order.setDetails(details);

        return Optional.of(order);
    }

    @Override
    public List<Order> getOrdersByDateAndAmount(LocalDate date, BigDecimal minAmount) {
        List<Order> orders = jdbcTemplate.query(GET_ORDERS_BY_DATE_AND_AMOUNT_SQL, ORDER_ROW_MAPPER, date, minAmount);
        for (Order order : orders) {
            List<OrderDetail> orderDetails = jdbcTemplate.query(
                    GET_ORDER_DETAILS_BY_ORDER_ID_SQL,
                    new OrderDetailRowMapper(),
                    order.getId()
            );
            order.setDetails(orderDetails);
        }
        return orders;
    }

    @Override
    public List<Order> getOrdersExcludingProduct(Long productCode, LocalDate startDate, LocalDate endDate) {
        checkProductExists(productCode);
        List<Order> orders = jdbcTemplate.query(GET_ORDERS_EXCLUDING_PRODUCT_SQL, ORDER_ROW_MAPPER, startDate, endDate, productCode);
        for (Order order : orders) {
            List<OrderDetail> orderDetails = jdbcTemplate.query(
                    GET_ORDER_DETAILS_BY_ORDER_ID_SQL,
                    new OrderDetailRowMapper(),
                    order.getId()
            );
            order.setDetails(orderDetails);
        }
        return orders;
    }

    private void checkProductExists(Long productCode) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM order_details WHERE product_code = ?",
                Integer.class,
                productCode);
        if (count == 0) {
            throw new ProductNotFoundException("Товара с кодом " + productCode + " не существует.");
        }
    }
}
