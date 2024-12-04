package com.example.ordersservice.model;

import com.example.ordersservice.enums.DeliveryType;
import com.example.ordersservice.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long id;
    private String orderNumber;
    private BigDecimal totalAmount;
    private LocalDate orderDate;
    private String recipient;
    private String deliveryAddress;
    private PaymentType paymentType;
    private DeliveryType deliveryType;
    private List<OrderDetail> details;

    public void calculateTotalAmount() {
        if (details != null) {
            this.totalAmount = details.stream()
                    .map(detail -> detail.getUnitPrice()
                            .multiply(BigDecimal.valueOf(detail.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.totalAmount = BigDecimal.ZERO;
        }
    }
}