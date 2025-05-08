package com.kokanapp.order_service.model;


public enum OrderStatus {
    PLACED,      // Order has been placed but not yet processed
    SHIPPED,      // Order has been shipped
    DELIVERED,    // Order has been delivered
    CANCELLED     // Order has been cancelled
}

