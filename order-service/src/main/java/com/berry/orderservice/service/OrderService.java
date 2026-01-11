package com.berry.orderservice.service;

import com.berry.orderservice.entity.OrderEntity;
import com.berry.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // RESUME POINT: Fault Tolerance (Circuit Breaker)
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackPlaceOrder")
    public String placeOrder(OrderEntity order) {
        // 1. Save Order to Database
        order.setStatus("CREATED");
        OrderEntity savedOrder = orderRepository.save(order);

        // 2. Publish Event to Kafka
        String message = "ORDER_ID:" + savedOrder.getId();
        kafkaTemplate.send("order-topic", message);
        log.info("Order placed and event sent: {}", message);

        return "Order Placed Successfully with ID: " + savedOrder.getId();
    }

    // Fallback method if DB or Kafka fails
    public String fallbackPlaceOrder(OrderEntity order, Throwable t) {
        log.error("Order Service failed: {}", t.getMessage());
        return "Service is currently down. Please try again later.";
    }

    public List<OrderEntity> getAllOrder(){
        return orderRepository.findAll();

    }
}
