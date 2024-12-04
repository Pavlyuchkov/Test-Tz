package com.example.ordersservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderDetailRequestDTO {

    private Long productCode;
    private String productName;
    private Long quantity;
    private BigDecimal unitPrice;
}