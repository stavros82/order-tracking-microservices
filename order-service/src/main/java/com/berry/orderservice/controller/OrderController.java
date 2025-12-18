package com.berry.orderservice.controller;

import com.berry.orderservice.entity.OrderEntity;
import com.berry.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderEntity order) {
        String result = orderService.placeOrder(order);
        return ResponseEntity.ok(result);
    }

    @GetMapping("get-all")
    public ResponseEntity<List<OrderEntity>> getAllOrders(){
        List<OrderEntity> orders = orderService.getAllOrder();
        return ResponseEntity.ok(orders);

    }
}
