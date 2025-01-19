package com.betvictor.consumer;

import com.betvictor.dto.ProcessedRecord;
import com.betvictor.exception.ConsumerRecordProcessingException;
import com.betvictor.service.ReportService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaReportsConsumer {

    private final ReportService reportService;

    @KafkaListener(
            topics = "${spring.kafka.topics.words-processed.name}",
            groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void consume(String consumerRecord) {
        try {
            log.info("( {} ) Received report with value: {}", Thread.currentThread().getName(), consumerRecord);
            final ProcessedRecord report = new ObjectMapper().readValue(consumerRecord, new TypeReference<>() {
            });
            reportService.saveRecords(report);

        } catch (Exception e) {
            throw new ConsumerRecordProcessingException("Error while processing reports", e);
        }
    }
}
