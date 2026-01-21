package org.example.ecommercebackend.controller;

import org.example.ecommercebackend.dto.request.PaymentWebhookRequest;
import org.example.ecommercebackend.dto.response.MessageResponse;
import org.example.ecommercebackend.service.PaymentWebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling payment webhooks.
 * Receives callbacks from payment gateway (mock Razorpay).
 */
@RestController
@RequestMapping("/api/webhooks")
public class PaymentWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentWebhookController.class);

    private final PaymentWebhookService paymentWebhookService;

    public PaymentWebhookController(PaymentWebhookService paymentWebhookService) {
        this.paymentWebhookService = paymentWebhookService;
    }

    /**
     * Receive payment webhook callback from Razorpay (mock).
     * POST /api/webhooks/payment
     */
    @PostMapping("/payment")
    public ResponseEntity<MessageResponse> handlePaymentWebhook(@RequestBody PaymentWebhookRequest request) {
        logger.info("Received payment webhook: orderId={}, status={}", 
                request.getRazorpayOrderId(), request.getStatus());

        paymentWebhookService.processWebhook(request);

        return ResponseEntity.ok(MessageResponse.of("Webhook processed successfully"));
    }
}

