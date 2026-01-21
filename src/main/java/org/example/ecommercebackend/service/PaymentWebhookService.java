package org.example.ecommercebackend.service;

import org.example.ecommercebackend.dto.request.PaymentWebhookRequest;
import org.example.ecommercebackend.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for handling payment webhook callbacks.
 */
@Service
public class PaymentWebhookService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentWebhookService.class);

    private final PaymentService paymentService;

    public PaymentWebhookService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Process the payment webhook callback.
     * Handles idempotency to prevent duplicate processing.
     */
    public void processWebhook(PaymentWebhookRequest request) {
        logger.info("Processing webhook for Razorpay order: {}, status: {}", 
                request.getRazorpayOrderId(), request.getStatus());

        // Validate webhook payload
        validateWebhookPayload(request);

        // Process based on payment status
        if (request.isSuccess()) {
            paymentService.processPaymentSuccess(request.getRazorpayOrderId(), request.getRazorpayPaymentId());
            logger.info("Successfully processed payment success webhook for order: {}", request.getRazorpayOrderId());

        } else if (request.isFailed()) {
            String reason = request.getMessage() != null ? request.getMessage() : "Payment failed";
            paymentService.processPaymentFailure(request.getRazorpayOrderId(), reason);
            logger.info("Successfully processed payment failure webhook for order: {}", request.getRazorpayOrderId());

        } else {
            logger.warn("Unknown payment status received: {}", request.getStatus());
            throw new BadRequestException("Unknown payment status: " + request.getStatus());
        }
    }

    /**
     * Validate the webhook payload for required fields.
     */
    private void validateWebhookPayload(PaymentWebhookRequest request) {
        if (request.getRazorpayOrderId() == null || request.getRazorpayOrderId().isBlank()) {
            logger.error("Webhook validation failed: missing razorpayOrderId");
            throw new BadRequestException("Invalid webhook payload: razorpayOrderId is required");
        }

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            logger.error("Webhook validation failed: missing status");
            throw new BadRequestException("Invalid webhook payload: status is required");
        }

        if (request.isSuccess() && (request.getRazorpayPaymentId() == null || request.getRazorpayPaymentId().isBlank())) {
            logger.error("Webhook validation failed: missing razorpayPaymentId for successful payment");
            throw new BadRequestException("Invalid webhook payload: razorpayPaymentId is required for successful payments");
        }
    }
}

