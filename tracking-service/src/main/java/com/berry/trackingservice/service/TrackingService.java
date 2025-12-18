package com.berry.trackingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingService {

    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // RESUME POINT: Multithreading (@Async)
    // This runs in a separate thread, freeing up the Kafka listener
    @Async("taskExecutor")
    public void processOrder(String orderId) {
        try {
            log.info("Processing order {} in thread: {}", orderId, Thread.currentThread().getName());

            cacheStatus(orderId, "STARTED");
            updateStatus(orderId, "STARTED");

            Thread.sleep(5000);

            // Step 1: Update status to PROCESSING
            cacheStatus(orderId, "PROCESSING");
            updateStatus(orderId,"PROCESSING");

            // Simulate long-running logistics task (e.g., contacting courier API)
            Thread.sleep(5000);

            // Step 2: Update status to SHIPPED
            cacheStatus(orderId, "SHIPPED");
            updateStatus(orderId, "SHIPPED");

            Thread.sleep(5000);

            cacheStatus(orderId, "DELIVERED");
            updateStatus(orderId, "DELIVERED");


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // RESUME POINT: Redis Caching
    public void cacheStatus(String orderId, String status) {
        redisTemplate.opsForValue().set("order:" + orderId, status);
        log.info("Redis Updated: Order {} -> {}", orderId, status);
    }

    public String getStatus(String orderId) {
        String status = redisTemplate.opsForValue().get("order:" + orderId);
        return status != null ? status : "NOT_FOUND";
    }

    private void updateStatus(String orderId, String status) {
        // 1. Update Redis (Fast Read)
        redisTemplate.opsForValue().set("order:" + orderId, status);

        // 2. Publish Update Event to Kafka (For Order Service to hear)
        // Format: "ID:STATUS" -> e.g., "101:SHIPPED"
        String eventMessage = orderId + ":" + status;
        kafkaTemplate.send("order-status-topic", eventMessage);

        log.info("Updated status for Order {} to {}", orderId, status);
    }
}
