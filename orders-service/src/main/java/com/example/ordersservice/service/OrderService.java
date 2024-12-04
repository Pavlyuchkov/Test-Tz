package com.example.ordersservice.service;

import com.example.ordersservice.dto.request.OrderRequestDTO;
import com.example.ordersservice.dto.response.OrderResponseDTO;
import com.example.ordersservice.exception.NoSuchOrdersException;
import com.example.ordersservice.exception.OrderNotFoundException;
import com.example.ordersservice.mapper.OrderMapper;
import com.example.ordersservice.model.Order;
import com.example.ordersservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final GeneratedNumbersPullService generatedNumbersPullService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public void createOrder(OrderRequestDTO orderRequestDTO) {
        String orderNumber = generatedNumbersPullService.getOrderNumber();
        Order order = orderMapper.orderRequestDTOToOrder(orderRequestDTO);
        order.calculateTotalAmount();
        order.setOrderNumber(orderNumber);
        orderRepository.createOrder(order);
    }

    public OrderResponseDTO getOrderById(Long id) {
        Optional<Order> order = orderRepository.getOrderById(id);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("Заказ не найден!");
        }
        return orderMapper.orderToOrderResponseDTO(order.get());
    }

    public List<OrderResponseDTO> getOrdersByDateAndAmount(LocalDate date, BigDecimal minAmount) {
        List<Order> orders = orderRepository.getOrdersByDateAndAmount(date, minAmount);
        if (orders.isEmpty()) {
            throw new NoSuchOrdersException("Нет заказов, соответствующих условию.");
        }
        return orders.stream()
                .map(orderMapper::orderToOrderResponseDTO)
                .toList();
    }

    public List<OrderResponseDTO> getOrdersExcludingProduct(Long productCode, LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.getOrdersExcludingProduct(productCode, startDate, endDate);
        if (orders.isEmpty()) {
            throw new NoSuchOrdersException("Нет заказов в заданном интервале.");
        }
        return orders.stream()
                .map(orderMapper::orderToOrderResponseDTO)
                .toList();
    }
}