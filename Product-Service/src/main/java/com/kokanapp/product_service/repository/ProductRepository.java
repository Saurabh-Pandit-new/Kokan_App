package com.kokanapp.product_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kokanapp.product_service.entity.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findBySellerId(Long sellerId);
    List<Product> findBySellerId(String sellerId);

}

