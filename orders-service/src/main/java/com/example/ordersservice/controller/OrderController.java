package com.example.ordersservice.controller;

import com.example.ordersservice.dto.request.OrderRequestDTO;
import com.example.ordersservice.dto.response.OrderResponseDTO;
import com.example.ordersservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        orderService.createOrder(orderRequestDTO);
        return ResponseEntity.status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_ENCODING, "UTF-8")
                .body("Заказ успешно оформлен!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        OrderResponseDTO orderResponseDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(orderResponseDTO);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<OrderResponseDTO>> getOrders(
            @RequestParam LocalDate date,
            @RequestParam BigDecimal minAmount) {
        List<OrderResponseDTO> orders = orderService.getOrdersByDateAndAmount(date, minAmount);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/excludes")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersExcludingProduct(
            @RequestParam Long productCode,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<OrderResponseDTO> orders = orderService.getOrdersExcludingProduct(productCode, startDate, endDate);
        return ResponseEntity.ok(orders);
    }
}
