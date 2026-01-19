package org.example.ecommercebackend.service;

import org.example.ecommercebackend.dto.request.CreatePaymentRequest;
import org.example.ecommercebackend.dto.response.PaymentResponse;
import org.example.ecommercebackend.model.Payment;

/**
 * Service interface for Payment operations.
 */
public interface PaymentService {

    /**
     * Initiate a payment for an order.
     */
    PaymentResponse initiatePayment(CreatePaymentRequest request);

    /**
     * Get payment by ID.
     */
    PaymentResponse getPaymentById(String paymentId);

    /**
     * Get payment by order ID.
     */
    PaymentResponse getPaymentByOrderId(String orderId);

    /**
     * Get payment entity by Razorpay order ID.
     */
    Payment getPaymentByRazorpayOrderId(String razorpayOrderId);

    /**
     * Process successful payment callback.
     */
    void processPaymentSuccess(String razorpayOrderId, String razorpayPaymentId);

    /**
     * Process failed payment callback.
     */
    void processPaymentFailure(String razorpayOrderId, String reason);
}

