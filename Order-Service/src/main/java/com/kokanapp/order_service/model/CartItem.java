package com.kokanapp.order_service.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private int quantity;
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)  // Lazy loading to optimize performance
    @JoinColumn(name = "cart_user_id")
    @JsonBackReference  // Prevents serialization of the Cart object inside CartItem
    private Cart cart;
}
