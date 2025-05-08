package com.kokanapp.product_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kokanapp.product_service.dto.ProductRequest;
import com.kokanapp.product_service.entity.Product;
import com.kokanapp.product_service.service.ImageUploadService;
import com.kokanapp.product_service.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ImageUploadService imageUploadService;
    
    public ProductController(ProductService productService, ImageUploadService imageUploadService) {
        this.productService = productService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(
        @RequestPart("product") ProductRequest request, // Product details
        @RequestPart("image") MultipartFile imageFile,  // Image file
        @RequestHeader(value = "X-User-Id", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role) {
        
        // Default values for userId and role
        if (userId == null) userId = 123L;
        if (role == null) role = "SELLER";

        // Check for role
        if (!"SELLER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            // Upload image to Cloudinary
            String imageUrl = imageUploadService.uploadImage(imageFile);

            // Set image URL in the request
            request.setImageUrls(List.of(imageUrl));

            // Add product to the database
            Product product = productService.addProduct(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (Exception e) {
            // Handle error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Product>> getProductsBySellerId(@PathVariable String sellerId) {
        List<Product> products = productService.getProductsBySellerId(sellerId);
        return ResponseEntity.ok(products);
    }

}
