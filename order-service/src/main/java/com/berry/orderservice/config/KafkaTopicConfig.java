package com.berry.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderTopic() {
        // RESUME POINT: Automated Infrastructure Setup
        // This automatically creates the topic if it doesn't exist
        return TopicBuilder.name("order-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
