package com.kokanapp.order_service.repository;


import com.kokanapp.order_service.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Custom queries can be added here if needed
}
