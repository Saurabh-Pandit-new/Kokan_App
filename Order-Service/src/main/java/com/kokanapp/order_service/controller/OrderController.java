package com.kokanapp.order_service.controller;

import com.kokanapp.order_service.model.Order;
import com.kokanapp.order_service.model.OrderStatus;
import com.kokanapp.order_service.service.OrderService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{userId}/place/item")
    public ResponseEntity<Order> placeSingleItemOrder(
            @PathVariable Long userId,
            @RequestParam String productId,
            @RequestHeader("Authorization") String token) {
        try {
            Order order = orderService.placeSingleItemOrder(userId, productId, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
