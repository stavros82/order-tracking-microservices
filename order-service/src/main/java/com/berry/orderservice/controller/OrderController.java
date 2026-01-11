package com.berry.orderservice.controller;

import com.berry.orderservice.dto.OrderRequest;
import com.berry.orderservice.dto.OrderResponse;
import com.berry.orderservice.entity.OrderEntity;
import com.berry.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderEntity saved = orderService.placeOrder(orderRequest);
        return ResponseEntity.ok(new OrderResponse(saved.getId(), "Order Placed Successfully"));
    }

    @GetMapping("get-all")
    public ResponseEntity<List<OrderEntity>> getAllOrders(){
        List<OrderEntity> orders = orderService.getAllOrder();
        return ResponseEntity.ok(orders);

    }
}
