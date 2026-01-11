package com.berry.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderRequest {

    @NotBlank(message = "Product is required")
    private String product;

    @Positive(message = "Price must be greater than zero")
    private Double price;
}

