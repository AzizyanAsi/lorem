package com.betvictor.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.time.Duration;

@Configuration
class KafkaConfig {

    @Value("${spring.kafka.topics.words-processed.name}")
    private String topic;
    @Value("${spring.kafka.topics.words-processed.partitions}")
    private int partitions;
    @Value("${spring.kafka.topics.words-processed.retention}")
    private Duration retention;

    @Bean
    NewTopic wordsProcessedKafkaTopic() {

        return TopicBuilder
                .name(topic)
                .partitions(partitions)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(retention.toMillis()))
                .build();
    }
}
