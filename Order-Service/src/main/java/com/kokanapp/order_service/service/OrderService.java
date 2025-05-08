package com.kokanapp.order_service.service;

import com.kokanapp.order_service.dto.ProductDTO;
import com.kokanapp.order_service.model.Order;
import com.kokanapp.order_service.model.OrderItem;
import com.kokanapp.order_service.model.OrderStatus;
import com.kokanapp.order_service.repository.OrderItemRepository;
import com.kokanapp.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://user-service/api/users/";
    private static final String PRODUCT_SERVICE_URL = "http://product-service/api/products/";

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
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Unauthorized or user not found");
        }
    }

    private ProductDTO getProductDetails(String productId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ProductDTO> response = restTemplate.exchange(
            PRODUCT_SERVICE_URL + productId,
            HttpMethod.GET,
            entity,
            ProductDTO.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Product not found");
        }

        return response.getBody();
    }

    @Transactional
    public Order placeSingleItemOrder(Long userId, String productId, String token) {
        // 1. Validate user
        validateUser(userId, token);

        // 2. Get product info
        ProductDTO product = getProductDetails(productId, token);

        // 3. Create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(product.getProductId());
        orderItem.setPrice(product.getPrice());
        orderItem.setQuantity(1);
        orderItem.setSellerId(product.getSellerId());

        // 4. Create order with item
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);
        order.setItems(List.of(orderItem));  // 👈 Add the item to the order's list
        orderItem.setOrder(order); // 👈 Important: link back

        // 5. Save order (cascades save to orderItem)
        Order savedOrder = orderRepository.save(order);

        return savedOrder;
    }

}
