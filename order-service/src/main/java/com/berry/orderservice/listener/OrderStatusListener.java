package com.berry.orderservice.listener;

import com.berry.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusListener {
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "order-status-topic", groupId = "order-group")
    public void updateOrderStatus(String message) {
        // Message format: "101:SHIPPED"
        String[] parts = message.split(":");
        Long orderId = Long.parseLong(parts[0]);
        String newStatus = parts[1];

        // Update DB
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(newStatus);
            orderRepository.save(order);
            log.info("Database Updated: Order {} is now {}", orderId, newStatus);
        });
    }
}
