package com.example.ordersservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentType {

    CARD("Карта"),
    CASH("Наличные");

    private final String description;
}
