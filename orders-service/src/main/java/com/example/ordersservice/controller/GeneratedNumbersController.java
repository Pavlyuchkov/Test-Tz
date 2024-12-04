package com.example.ordersservice.controller;

import com.example.ordersservice.service.GeneratedNumbersPullService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/numbers")
@AllArgsConstructor
public class GeneratedNumbersController {

    private final GeneratedNumbersPullService generatedNumbersPullService;

    @PostMapping("/request")
    public ResponseEntity<Void> receiveOrderNumbers(@RequestBody List<String> orderNumbers) {
        generatedNumbersPullService.addOrderNumbers(orderNumbers);
        return ResponseEntity.ok().build();
    }
}