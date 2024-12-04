package com.example.ordersservice.mapper;

import com.example.ordersservice.dto.request.OrderRequestDTO;
import com.example.ordersservice.dto.response.OrderResponseDTO;
import com.example.ordersservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    @Mapping(source = "orderNumber", target = "orderNumber")
    @Mapping(source = "totalAmount", target = "totalAmount")
    @Mapping(source = "paymentType", target = "paymentType")
    @Mapping(source = "deliveryType", target = "deliveryType")
    @Mapping(source = "details", target = "details")
    OrderResponseDTO orderToOrderResponseDTO(Order order);

    @Mapping(source = "paymentType", target = "paymentType")
    @Mapping(source = "deliveryType", target = "deliveryType")
    @Mapping(source = "details", target = "details")
    Order orderRequestDTOToOrder(OrderRequestDTO orderRequestDTO);
}