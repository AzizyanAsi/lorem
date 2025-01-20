package com.betvictor.producer;

import com.betvictor.dto.ReportDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@SpringBootTest
class KafkaMessageProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaMessageProducer kafkaMessageProducer;

    private static final String TOPIC = "test-topic";

    @BeforeEach
    void setUp() {
        // Setting the topic value using reflection
        try {
            var topicField = KafkaMessageProducer.class.getDeclaredField("topic");
            topicField.setAccessible(true);
            topicField.set(kafkaMessageProducer, TOPIC);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set topic field in KafkaMessageProducer", e);
        }
    }

    @Test
    void sendMessage_validReport_shouldSendMessage() throws Exception {
        // Arrange
        ReportDto report = new ReportDto("sample", (short) 5, 1.2, 2.3);
        String reportJson = """
                {"freq_word":"sample","avg_paragraph_size":5,"avg_paragraph_processing_time":1.2,"total_processing_time":2.3}""";

        when(objectMapper.writeValueAsString(report)).thenReturn(reportJson);

        kafkaMessageProducer.sendMessage(report);

        verify(kafkaTemplate, times(1)).send(TOPIC, report.freqWord(), reportJson);
    }
}
