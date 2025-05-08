package com.kokanapp.order_service.controller;

import com.kokanapp.order_service.model.Cart;
import com.kokanapp.order_service.model.CartItem;
import com.kokanapp.order_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Get the cart for a specific user
    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
        return cartService.getCart(userId, token);
    }

    // Add an item to the cart
    @PostMapping("/{userId}/add")
    public Cart addItem(@PathVariable Long userId,
                        @RequestBody CartItem item,
                        @RequestHeader("Authorization") String token) {
        return cartService.addToCart(userId, item, token);
    }

    // Update quantity of an existing item in the cart
    @PutMapping("/{userId}/update")
    public Cart updateItem(@PathVariable Long userId,
                           @RequestParam String productId,  // Updated to String
                           @RequestParam int quantity,
                           @RequestHeader("Authorization") String token) {
        return cartService.updateCartItem(userId, productId, quantity, token);
    }

    // Remove an item from the cart
    @DeleteMapping("/{userId}/remove")
    public Cart removeItem(@PathVariable Long userId,
                           @RequestParam String productId,  // Updated to String
                           @RequestHeader("Authorization") String token) {
        return cartService.removeItem(userId, productId, token);
    }

    // Clear all items from the cart
    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable Long userId,
                          @RequestHeader("Authorization") String token) {
        cartService.clearCart(userId, token);
    }
}
