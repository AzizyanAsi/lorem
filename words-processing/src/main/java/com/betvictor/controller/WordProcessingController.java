package com.betvictor.controller;

import com.betvictor.dto.ReportDto;
import com.betvictor.enums.LengthType;
import com.betvictor.producer.KafkaMessageProducer;
import com.betvictor.service.WordProcessingService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/betvictor")
@RequiredArgsConstructor
public class WordProcessingController {


    private final WordProcessingService wordProcessingService;

    private final KafkaMessageProducer producerService;

    @GetMapping("/text")
    public ResponseEntity<ReportDto> processText(@Min(1) @RequestParam("p") final int paragraphs,
                                                 @RequestParam("l") final LengthType length) {

        log.info("Processing request with paragraphs={} and length={}", paragraphs, length);
        final ReportDto report = wordProcessingService.processText(paragraphs, length);

        // Produce to Kafka
        producerService.sendMessage(report);
        log.info("Produced message to Kafka: report={}", report);

        return ResponseEntity.ok(report);
    }
}
