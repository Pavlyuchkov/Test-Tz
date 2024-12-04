package com.example.ordersservice.service;

import com.example.ordersservice.dto.request.OrderDetailRequestDTO;
import com.example.ordersservice.dto.request.OrderRequestDTO;
import com.example.ordersservice.dto.response.OrderResponseDTO;
import com.example.ordersservice.exception.NoSuchOrdersException;
import com.example.ordersservice.exception.OrderNotFoundException;
import com.example.ordersservice.mapper.OrderMapper;
import com.example.ordersservice.model.Order;
import com.example.ordersservice.model.OrderDetail;
import com.example.ordersservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private GeneratedNumbersPullService generatedNumbersPullService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Test
    void testCreateOrder() {
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setDetails(List.of(
                new OrderDetailRequestDTO(123L, "Товар 1", 2L, BigDecimal.valueOf(100))
        ));

        Order order = new Order();
        order.setDetails(List.of(
                new OrderDetail(123L, 123L, "Товар 1", 2L, BigDecimal.valueOf(100), null)
        ));
        order.setTotalAmount(BigDecimal.valueOf(200));

        Mockito.when(generatedNumbersPullService.getOrderNumber()).thenReturn("тестовый заказ");
        Mockito.when(orderMapper.orderRequestDTOToOrder(requestDTO)).thenReturn(order);
        
        orderService.createOrder(requestDTO);
        
        Mockito.verify(orderMapper).orderRequestDTOToOrder(requestDTO);
        Mockito.verify(orderRepository).createOrder(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();
        assertEquals("тестовый заказ", capturedOrder.getOrderNumber());
        assertEquals(BigDecimal.valueOf(200), capturedOrder.getTotalAmount());
    }

    @Test
    void testGetOrderById_OrderExists() {
        Long orderId = 1L;
        Order order = new Order();
        order.setOrderNumber("тестовый заказ");

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderNumber("тестовый заказ");

        Mockito.when(orderRepository.getOrderById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.getOrderById(orderId);

        assertEquals("тестовый заказ", result.getOrderNumber());
        Mockito.verify(orderRepository).getOrderById(orderId);
        Mockito.verify(orderMapper).orderToOrderResponseDTO(order);
    }

    @Test
    void testGetOrderById_OrderNotFound() {
        Long orderId = 1L;

        Mockito.when(orderRepository.getOrderById(orderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(orderId));

        assertEquals("Заказ не найден!", exception.getMessage());
        Mockito.verify(orderRepository).getOrderById(orderId);
        Mockito.verifyNoInteractions(orderMapper);
    }

    @Test
    void testGetOrdersByDateAndAmount_OrdersExist() {
        LocalDate date = LocalDate.now();
        BigDecimal minAmount = BigDecimal.valueOf(100);

        Order order = new Order();
        order.setOrderNumber("тестовый заказ");

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderNumber("тестовый заказ");

        Mockito.when(orderRepository.getOrdersByDateAndAmount(date, minAmount)).thenReturn(List.of(order));
        Mockito.when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(responseDTO);

        List<OrderResponseDTO> results = orderService.getOrdersByDateAndAmount(date, minAmount);

        assertEquals(1, results.size());
        assertEquals("тестовый заказ", results.get(0).getOrderNumber());
        Mockito.verify(orderRepository).getOrdersByDateAndAmount(date, minAmount);
        Mockito.verify(orderMapper).orderToOrderResponseDTO(order);
    }

    @Test
    void testGetOrdersByDateAndAmount_NoOrdersFound() {
        LocalDate date = LocalDate.now();
        BigDecimal minAmount = BigDecimal.valueOf(100);

        Mockito.when(orderRepository.getOrdersByDateAndAmount(date, minAmount)).thenReturn(List.of());

        Exception exception = assertThrows(NoSuchOrdersException.class,
                () -> orderService.getOrdersByDateAndAmount(date, minAmount));

        assertEquals("Нет заказов, соответствующих условию.", exception.getMessage());
        Mockito.verify(orderRepository).getOrdersByDateAndAmount(date, minAmount);
        Mockito.verifyNoInteractions(orderMapper);
    }

    @Test
    void testGetOrdersExcludingProduct() {
        Long productCode = 123L;
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        Order order = new Order();
        order.setOrderNumber("тестовый заказ");

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderNumber("тестовый заказ");

        Mockito.when(orderRepository.getOrdersExcludingProduct(productCode, startDate, endDate)).thenReturn(List.of(order));
        Mockito.when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(responseDTO);

        List<OrderResponseDTO> results = orderService.getOrdersExcludingProduct(productCode, startDate, endDate);

        assertEquals(1, results.size());
        assertEquals("тестовый заказ", results.get(0).getOrderNumber());
        Mockito.verify(orderRepository).getOrdersExcludingProduct(productCode, startDate, endDate);
        Mockito.verify(orderMapper).orderToOrderResponseDTO(order);
    }
}
