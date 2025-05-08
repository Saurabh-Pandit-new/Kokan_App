package com.kokanapp.product_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kokanapp.product_service.dto.ProductRequest;
import com.kokanapp.product_service.entity.Category;
import com.kokanapp.product_service.entity.Product;
import com.kokanapp.product_service.repository.CategoryRepository;
import com.kokanapp.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	@Autowired
    private ProductRepository productRepository;
	@Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product addProduct(ProductRequest request, Long sellerId) {
        // Check or create category
        Category category = categoryRepository.findByName(request.getCategory())
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName(request.getCategory());
                    return categoryRepository.save(newCat);
                });

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setIsAvailable(request.getQuantity() > 0);
        product.setImageUrls(request.getImageUrls());
        product.setAttributes(request.getAttributes());

        product.setSellerId(sellerId);
        product.setCategoryId(category.getId());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public List<Product> getProductsBySellerId(String sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

}

