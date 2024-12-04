package com.example.ordersservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    private Long id;
    private Long productCode;
    private String productName;
    private Long quantity;
    private BigDecimal unitPrice;
    private Long orderId;
}