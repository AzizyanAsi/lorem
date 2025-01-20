package com.betvictor.consumer;

import com.betvictor.dto.ProcessedRecord;
import com.betvictor.exception.ConsumerRecordProcessingException;
import com.betvictor.service.ReportService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class KafkaReportsConsumerTest {

    @Mock
    private ReportService reportService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaReportsConsumer kafkaReportsConsumer;

    @Test
    void consume_validRecord_shouldSaveReport() throws Exception {
        // Arrange
        String consumerRecord = """
                {
                "freq_word":"sample",
                "avg_paragraph_size":5,
                "avg_paragraph_processing_time":1.2,
                "total_processing_time":2.3
                }
                """;
        ProcessedRecord processedRecord = ProcessedRecord.builder()
                .freqWord("sample")
                .avgParagraphSize((short) 5)
                .avgParagraphProcessingTime(1.2)
                .totalProcessingTime(2.3)
                .build();

        when(objectMapper.readValue(eq(consumerRecord), any(TypeReference.class))).thenReturn(processedRecord);

        kafkaReportsConsumer.consume(consumerRecord);

        verify(reportService, times(1)).saveRecords(processedRecord);
    }

    @Test
    void consume_invalidRecord_shouldThrowException() throws Exception {
        // Arrange
        String consumerRecord = "invalid-json";

        when(objectMapper.readValue(eq(consumerRecord), any(TypeReference.class)))
                .thenThrow(new RuntimeException("JSON parsing error"));

        ConsumerRecordProcessingException exception = assertThrows(ConsumerRecordProcessingException.class, () -> {
            kafkaReportsConsumer.consume(consumerRecord);
        });

        assertTrue(exception.getMessage().contains("Error while processing reports"));
        verify(reportService, never()).saveRecords(any());
    }
}
