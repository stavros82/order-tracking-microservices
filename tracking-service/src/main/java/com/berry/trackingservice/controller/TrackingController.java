package com.berry.trackingservice.controller;

import com.berry.trackingservice.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @GetMapping("/{orderId}")
    public String getOrderStatus(@PathVariable String orderId) {
        return trackingService.getStatus(orderId);
    }
}
