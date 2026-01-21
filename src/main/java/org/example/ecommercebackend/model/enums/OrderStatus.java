package org.example.ecommercebackend.model.enums;

/**
 * Enum representing the status of an order.
 */
public enum OrderStatus {
    
    CREATED,    // Order has been created, pending payment
    PAID,       // Payment successful
    FAILED,     // Payment failed
    CANCELLED   // Order was cancelled
}
