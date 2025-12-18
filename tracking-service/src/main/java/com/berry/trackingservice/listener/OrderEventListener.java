package com.berry.trackingservice.listener;

import com.berry.trackingservice.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final TrackingService trackingService;

    // RESUME POINT: Event-Driven Architecture
    @KafkaListener(topics = "order-topic", groupId = "tracking-group")
    public void handleOrderEvent(String message) {
        log.info("Kafka Event Received: {}", message);

        // Extract ID (Message format: "ORDER_ID:123")
        String orderId = message.split(":")[1];

        // Trigger Async Processing
        trackingService.processOrder(orderId);
    }
}
