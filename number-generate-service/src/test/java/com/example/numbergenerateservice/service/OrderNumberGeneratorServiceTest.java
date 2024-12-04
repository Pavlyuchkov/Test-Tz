package com.example.numbergenerateservice.service;

import com.example.numbergenerateservice.repository.OrderNumberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderNumberGeneratorServiceTest {

    @Mock
    private OrderNumberRepository orderNumberRepository;

    @Mock
    private OrderNumberBatchSenderService batchSenderService;

    @InjectMocks
    private OrderNumberGeneratorService orderNumberGeneratorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateOrderNumber_ShouldReturnCorrectOrderNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        when(orderNumberRepository.incrementCounterAndResetDate(
                OrderNumberGeneratorService.ORDER_NUMBER_KEY,
                OrderNumberGeneratorService.LAST_RESET_DATE_KEY,
                datePart
        )).thenReturn(1L);

        String result = orderNumberGeneratorService.generateOrderNumber();

        assertEquals("0000120241204", result);
        verify(orderNumberRepository, times(1)).incrementCounterAndResetDate(
                OrderNumberGeneratorService.ORDER_NUMBER_KEY,
                OrderNumberGeneratorService.LAST_RESET_DATE_KEY,
                datePart
        );
    }

    @Test
    void testGenerateAndSendBatch() {
        List<String> mockOrderNumbers = List.of("00001", "00002", "00003");

        doNothing().when(orderNumberRepository).saveNumbersBatch(eq("ORDER_NUMBER_BATCH_KEY"), eq(mockOrderNumbers));
        doNothing().when(orderNumberRepository).setBatchTTL(eq("ORDER_NUMBER_BATCH_KEY"), eq(120));

        when(orderNumberRepository.incrementCounterAndResetDate(eq("order_number_counter"), eq("last_reset_date"), anyString()))
                .thenReturn(1L);

        doNothing().when(batchSenderService).sendBatchOfOrderNumbers();

        orderNumberGeneratorService.createAndSaveBatch();

        verify(orderNumberRepository, times(1000)).incrementCounterAndResetDate(eq("order_number_counter"), eq("last_reset_date"), anyString());
        verify(batchSenderService, times(1)).sendBatchOfOrderNumbers();
    }
}