package com.example.ordersservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryType {

    PICKUP("Самовывоз"),
    DOOR_DELIVERY("Доставка до двери");

    private final String description;
}