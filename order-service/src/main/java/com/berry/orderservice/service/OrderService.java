package com.berry.orderservice.service;

import com.berry.orderservice.dto.OrderRequest;
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
    public OrderEntity placeOrder(OrderRequest request) {
        // map DTO to entity
        OrderEntity order = new OrderEntity();
        order.setProduct(request.getProduct());
        order.setPrice(request.getPrice() != null ? request.getPrice() : 0.0);

        // 1. Save Order to Database
        order.setStatus("CREATED");
        OrderEntity savedOrder = orderRepository.save(order);

        // 2. Publish Event to Kafka
        String message = "ORDER_ID:" + savedOrder.getId();
        kafkaTemplate.send("order-topic", message);
        log.info("Order placed and event sent: {}", message);

        return savedOrder;
    }

    // Fallback method if DB or Kafka fails - signature must match original plus Throwable
    public OrderEntity fallbackPlaceOrder(OrderRequest request, Throwable t) {
        log.error("Order Service failed: {}", t.getMessage());
        // return a placeholder entity indicating failure
        OrderEntity failed = new OrderEntity();
        failed.setProduct(request != null ? request.getProduct() : null);
        failed.setPrice(request != null && request.getPrice() != null ? request.getPrice() : 0.0);
        failed.setStatus("FAILED");
        return failed;
    }

    public List<OrderEntity> getAllOrder(){
        return orderRepository.findAll();

    }
}
