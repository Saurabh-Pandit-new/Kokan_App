package com.kokanapp.order_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long userId; // User who placed the order

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id") // This links the OrderItem to this Order (i.e., foreign key)
    private List<OrderItem> items;

    private LocalDateTime orderDate; // Order Date

    private OrderStatus status; // Order Status (e.g., PLACED, SHIPPED)

    // Getters and setters are automatically provided by Lombok
}
