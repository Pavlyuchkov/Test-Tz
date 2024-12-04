package com.example.numbergenerateservice.service;

import com.example.numbergenerateservice.repository.OrderNumberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderNumberGeneratorService {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final String ORDER_NUMBER_KEY = "order_number_counter";
    public static final String LAST_RESET_DATE_KEY = "last_reset_date";
    public static final String ORDER_NUMBER_BATCH_KEY = "order_number_batch";

    private final OrderNumberRepository orderNumberRepository;
    private final OrderNumberBatchSenderService batchSenderService;

    public String generateOrderNumber() {
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);
        Long uniquePart = orderNumberRepository.incrementCounterAndResetDate(ORDER_NUMBER_KEY, LAST_RESET_DATE_KEY, datePart);
        return String.format("%05d%s", uniquePart, datePart);
    }

    public void createAndSaveBatch() {
        List<String> orderNumbers = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            String orderNumber = generateOrderNumber();
            orderNumbers.add(orderNumber);
        }
        orderNumberRepository.saveNumbersBatch(ORDER_NUMBER_BATCH_KEY, orderNumbers);
        orderNumberRepository.setBatchTTL(ORDER_NUMBER_BATCH_KEY, 120);
        batchSenderService.sendBatchOfOrderNumbers();
    }
}