package com.kokanapp.order_service.service;

import com.kokanapp.order_service.dto.ProductDTO;
import com.kokanapp.order_service.model.Cart;
import com.kokanapp.order_service.model.CartItem;
import com.kokanapp.order_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;

    private static final String PRODUCT_SERVICE_URL = "http://product-service/api/products/";
    private static final String USER_SERVICE_URL = "http://user-service/api/users/";

    // ✅ Helper method to validate user token
    private void validateUser(Long userId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            restTemplate.exchange(
                USER_SERVICE_URL + userId,
                HttpMethod.GET,
                entity,
                Object.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("User ID " + userId + " not found");
        }
    }

    public Cart getCart(Long userId, String token) {
        validateUser(userId, token);
        return cartRepository.findById(userId)
                .orElseGet(() -> cartRepository.save(
                    Cart.builder()
                        .userId(userId)
                        .items(new ArrayList<>())
                        .build()
                ));
    }

    @Transactional
    public Cart addToCart(Long userId, CartItem newItem, String token) {
        validateUser(userId, token);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 🎯 Fetch product info
        ResponseEntity<ProductDTO> response = restTemplate.exchange(
            PRODUCT_SERVICE_URL + newItem.getProductId(),
            HttpMethod.GET,
            entity,
            ProductDTO.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Invalid product ID");
        }

        ProductDTO product = response.getBody();
        if (product.getQuantity() < newItem.getQuantity()) {
            throw new RuntimeException("Product out of stock");
        }

        newItem.setPrice(product.getPrice());

        Cart cart = getCart(userId, token);

        // ✅ Check if the item already exists — if so, update quantity
        boolean itemUpdated = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(newItem.getProductId())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                item.setPrice(product.getPrice()); // Optional: update price too
                itemUpdated = true;
                break;
            }
        }

        // ✅ If not found, add as new item
        if (!itemUpdated) {
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }



    @Transactional
    public Cart updateCartItem(Long userId, String productId, int newQty, String token) {
        validateUser(userId, token);
        Cart cart = getCart(userId, token);
        cart.getItems().forEach(item -> {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(newQty);
            }
        });
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(Long userId, String productId, String token) {
        validateUser(userId, token);
        Cart cart = getCart(userId, token);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long userId, String token) {
        validateUser(userId, token);
        Cart cart = getCart(userId, token);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
