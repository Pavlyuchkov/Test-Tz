package com.example.ordersservice.dto.request;

import com.example.ordersservice.enums.DeliveryType;
import com.example.ordersservice.enums.PaymentType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequestDTO {

    private LocalDate orderDate;
    private String recipient;
    private String deliveryAddress;
    private PaymentType paymentType;
    private DeliveryType deliveryType;
    private List<OrderDetailRequestDTO> details;
}