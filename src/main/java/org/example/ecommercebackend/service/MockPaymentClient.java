package org.example.ecommercebackend.service;

import org.example.ecommercebackend.dto.request.MockPaymentRequest;
import org.example.ecommercebackend.dto.response.MockPaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.UUID;

/**
 * Mock Razorpay payment gateway client.
 * Simulates async payment processing with configurable delay and success rate.
 */
@Service
public class MockPaymentClient {

    private static final Logger logger = LoggerFactory.getLogger(MockPaymentClient.class);

    private final RestTemplate restTemplate;
    private final Random random = new Random();

    @Value("${mock.payment.delay-ms:3000}")
    private long paymentDelayMs;

    @Value("${mock.payment.success-rate:0.8}")
    private double successRate;

    @Value("${server.port:8080}")
    private String serverPort;

    public MockPaymentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Process payment asynchronously.
     * Simulates payment gateway behavior with configurable delay.
     * After processing, calls the webhook endpoint with the result.
     */
    @Async
    public void processPaymentAsync(MockPaymentRequest request) {
        logger.info("Processing payment for Razorpay order: {}", request.getRazorpayOrderId());

        try {
            // Simulate payment processing delay
            Thread.sleep(paymentDelayMs);

            // Simulate success/failure based on success rate
            boolean isSuccess = random.nextDouble() < successRate;

            MockPaymentResponse response;
            if (isSuccess) {
                String razorpayPaymentId = "pay_" + UUID.randomUUID().toString().replace("-", "").substring(0, 14);
                response = MockPaymentResponse.success(request.getRazorpayOrderId(), razorpayPaymentId);
                logger.info("Payment SUCCESS for order: {}, paymentId: {}", 
                        request.getRazorpayOrderId(), razorpayPaymentId);
            } else {
                response = MockPaymentResponse.failure(request.getRazorpayOrderId(), "Payment declined by bank");
                logger.info("Payment FAILED for order: {}", request.getRazorpayOrderId());
            }

            // Call webhook endpoint
            String webhookUrl = "http://localhost:" + serverPort + "/api/webhooks/payment";
            logger.info("Calling webhook at: {}", webhookUrl);

            restTemplate.postForEntity(webhookUrl, response, Void.class);
            logger.info("Webhook called successfully for order: {}", request.getRazorpayOrderId());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Payment processing interrupted for order: {}", request.getRazorpayOrderId());
        } catch (Exception e) {
            logger.error("Error processing payment for order: {}: {}", 
                    request.getRazorpayOrderId(), e.getMessage());
        }
    }

    /**
     * Initiate payment processing (called after payment is created).
     */
    public void initiatePayment(String razorpayOrderId, java.math.BigDecimal amount) {
        MockPaymentRequest request = MockPaymentRequest.of(
                razorpayOrderId,
                amount,
                "http://localhost:" + serverPort + "/api/webhooks/payment"
        );
        processPaymentAsync(request);
    }
}

