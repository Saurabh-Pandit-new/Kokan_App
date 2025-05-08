package com.kokanapp.order_service.util;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final RestTemplate restTemplate;

    private static final String PRODUCT_SERVICE_URL = "http://product-service/api/products/";

    // Helper method to validate product existence in the catalog
    public void validateProductExists(String productId) {
        try {
            restTemplate.exchange(PRODUCT_SERVICE_URL + productId, HttpMethod.GET, null, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Product with ID " + productId + " not found");
        }
    }
}

