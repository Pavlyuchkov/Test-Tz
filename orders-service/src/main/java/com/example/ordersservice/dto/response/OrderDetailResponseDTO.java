package com.example.ordersservice.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailResponseDTO {

    private Long productCode;
    private String productName;
    private Long quantity;
    private BigDecimal unitPrice;
}