package com.example.ordersservice.mapper;

import com.example.ordersservice.dto.request.OrderDetailRequestDTO;
import com.example.ordersservice.dto.request.OrderRequestDTO;
import com.example.ordersservice.dto.response.OrderResponseDTO;
import com.example.ordersservice.enums.DeliveryType;
import com.example.ordersservice.enums.PaymentType;
import com.example.ordersservice.model.Order;
import com.example.ordersservice.model.OrderDetail;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderMapperTest {

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Test
    void testOrderToOrderResponseDTO() {
        Order order = Order.builder()
                .orderNumber("testOrder")
                .orderDate(LocalDate.now())
                .recipient("Иван Иванов")
                .deliveryAddress("Москва, Кремль")
                .paymentType(PaymentType.CARD)
                .deliveryType(DeliveryType.PICKUP)
                .details(List.of(OrderDetail.builder().productCode(123L).productName("Товар 1").quantity(2L)
                        .unitPrice(BigDecimal.valueOf(100)).build()))
                .build();

        OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(order);

        assertEquals(order.getPaymentType(), orderResponseDTO.getPaymentType());
        assertEquals(order.getDeliveryType(), orderResponseDTO.getDeliveryType());
        assertEquals(order.getDetails().size(), orderResponseDTO.getDetails().size());
    }

    @Test
    void testOrderRequestDTOToOrder() {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setPaymentType(PaymentType.CARD);
        orderRequestDTO.setDeliveryType(DeliveryType.PICKUP);
        orderRequestDTO.setDetails(List.of(new OrderDetailRequestDTO(123L, "Товар 1", 2L, BigDecimal.valueOf(100))));

        Order order = orderMapper.orderRequestDTOToOrder(orderRequestDTO);

        assertEquals(orderRequestDTO.getPaymentType(), order.getPaymentType());
        assertEquals(orderRequestDTO.getDeliveryType(), order.getDeliveryType());
        assertEquals(orderRequestDTO.getDetails().size(), order.getDetails().size());
    }
}
