package com.example.ordersservice.dto.response;

import com.example.ordersservice.enums.DeliveryType;
import com.example.ordersservice.enums.PaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponseDTO {

    private String orderNumber;
    private BigDecimal totalAmount;
    private LocalDate orderDate;
    private String recipient;
    private String deliveryAddress;
    private PaymentType paymentType;
    private DeliveryType deliveryType;
    private List<OrderDetailResponseDTO> details;
}