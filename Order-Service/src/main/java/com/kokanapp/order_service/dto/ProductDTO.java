package com.kokanapp.order_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductDTO {
	@JsonProperty("id")
    private String productId;  // MongoDB stores productId as String
    private String name;
    private String description;
    private double price;
    private int quantity;
    private long sellerId;
}
