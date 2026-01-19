package org.example.ecommercebackend.repository;

import org.example.ecommercebackend.model.Payment;
import org.example.ecommercebackend.model.enums.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Payment entity operations.
 */
@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    /**
     * Find payment by order ID.
     */
    Optional<Payment> findByOrderId(String orderId);

    /**
     * Find all payments for an order.
     */
    List<Payment> findAllByOrderId(String orderId);

    /**
     * Find payment by Razorpay payment ID.
     */
    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

    /**
     * Find payment by Razorpay order ID.
     */
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    /**
     * Find payments by status.
     */
    List<Payment> findByStatus(PaymentStatus status);

    /**
     * Check if a payment exists for an order.
     */
    boolean existsByOrderId(String orderId);
}

