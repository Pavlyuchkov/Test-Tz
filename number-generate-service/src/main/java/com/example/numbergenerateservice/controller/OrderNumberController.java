package com.example.numbergenerateservice.controller;

import com.example.numbergenerateservice.service.OrderNumberGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generate")
@AllArgsConstructor
public class OrderNumberController {

    private final OrderNumberGeneratorService orderNumberGeneratorService;

    @PostMapping("/numbers")
    public ResponseEntity<String> generateNumbers() {
        orderNumberGeneratorService.createAndSaveBatch();
        return ResponseEntity.status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_ENCODING, "UTF-8")
                .body("Батч номеров успешно сгенерирован и отправлен на order-service!");
    }
}