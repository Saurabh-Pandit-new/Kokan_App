package com.kokanapp.order_service.repository;


import com.kokanapp.order_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // Long = userId (since Cart's primary key is userId)
}

