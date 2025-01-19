package com.betvictor.producer;

import com.betvictor.dto.ReportDto;
import com.betvictor.exceptions.ProducerRecordProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topics.words-processed.name}")
    private String topic;

    public void sendMessage(final ReportDto report) {
        try {
            final String reportJson = new ObjectMapper().writeValueAsString(report);
            kafkaTemplate.send(topic, report.freqWord(), reportJson);
            log.info("Message sent to Kafka topic {}: {}", topic, reportJson);
        } catch (Exception e) {
            log.error("Error sending message to Kafka topic {}: {}", topic, e.getMessage());

        }
    }
}

