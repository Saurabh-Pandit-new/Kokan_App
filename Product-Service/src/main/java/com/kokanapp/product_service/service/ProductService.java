package com.kokanapp.product_service.service;

import java.util.List;

import com.kokanapp.product_service.dto.ProductRequest;
import com.kokanapp.product_service.entity.Product;

public interface ProductService {
    Product addProduct(ProductRequest request, Long sellerId);
    
    List<Product> getAllProducts();

    Product getProductById(String id);


    List<Product> getProductsBySellerId(String userId);
}
