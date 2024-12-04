package com.example.ordersservice.repository;

import com.example.ordersservice.enums.DeliveryType;
import com.example.ordersservice.enums.PaymentType;
import com.example.ordersservice.exception.ProductNotFoundException;
import com.example.ordersservice.model.Order;
import com.example.ordersservice.model.OrderDetail;
import com.example.ordersservice.repository.impl.OrderRepositoryImpl;
import com.example.ordersservice.repository.rowMapper.OrderDetailRowMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class OrderRepositoryImplTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:14-alpine")
                    .withDatabaseName("db")
                    .withUsername("orders")
                    .withPassword("orders");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderRepositoryImpl orderRepository;

    @BeforeAll
    static void setUpContainer() {
        POSTGRES_CONTAINER.start();
        System.setProperty("spring.datasource.url", POSTGRES_CONTAINER.getJdbcUrl());
        System.setProperty("spring.datasource.username", POSTGRES_CONTAINER.getUsername());
        System.setProperty("spring.datasource.password", POSTGRES_CONTAINER.getPassword());
    }

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepositoryImpl(jdbcTemplate);
    }

    @Test
    void createOrder_shouldInsertOrderAndDetails() {
        Order order = new Order();
        order.setId(15L);
        order.setOrderNumber("ORD1007");
        order.setTotalAmount(BigDecimal.valueOf(300));
        order.setOrderDate(LocalDate.now());
        order.setRecipient("Иван Иванов");
        order.setDeliveryAddress("Москва");
        order.setPaymentType(PaymentType.CARD);
        order.setDeliveryType(DeliveryType.PICKUP);

        OrderDetail detail1 = new OrderDetail(25L, 101L, "Товар 1", 2L, BigDecimal.valueOf(100), 15L);
        OrderDetail detail2 = new OrderDetail(26L, 102L, "Товар 2", 1L, BigDecimal.valueOf(200), 15L);
        order.setDetails(List.of(detail1, detail2));

        orderRepository.createOrder(order);

        assertNotNull(order.getId());
        List<OrderDetail> details = jdbcTemplate.query(
                "SELECT * FROM order_details WHERE order_id = ?",
                new OrderDetailRowMapper(),
                order.getId()
        );
        assertEquals(2, details.size());
    }

    @Test
    void getOrderById_shouldReturnOrderWithDetails() {
        Optional<Order> orderOpt = orderRepository.getOrderById(1L);
        assertTrue(orderOpt.isPresent());

        Order order = orderOpt.get();
        assertEquals("ORD1001", order.getOrderNumber());
        assertEquals(3, order.getDetails().size());
    }

    @Test
    void getOrdersByDateAndAmount_shouldReturnFilteredOrders() {
        List<Order> orders = orderRepository.getOrdersByDateAndAmount(LocalDate.of(2024, 5, 5), BigDecimal.valueOf(30000));

        assertEquals(2, orders.size());

        assertTrue(orders.stream().anyMatch(order -> order.getOrderNumber().equals("ORD1004")));
        assertTrue(orders.stream().anyMatch(order -> order.getOrderNumber().equals("ORD1005")));
    }

    @Test
    void getOrdersExcludingProduct_shouldReturnOrdersWithoutSpecificProduct() {
        List<Order> orders = orderRepository.getOrdersExcludingProduct(103L, LocalDate.now().minusMonths(12), LocalDate.now());
        assertEquals(3, orders.size());
    }

    @Test
    void checkProductExists_shouldThrowExceptionIfProductNotFound() {
        assertThrows(ProductNotFoundException.class, () -> orderRepository.getOrdersExcludingProduct(999L, LocalDate.now(), LocalDate.now()));
    }
}