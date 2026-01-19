package org.example.ecommercebackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for payment webhook callback.
 * This represents the payload sent by Razorpay (mock) to our webhook endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentWebhookRequest {

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String status;
    private String message;

    /**
     * Check if the payment was successful.
     */
    public boolean isSuccess() {
        return "SUCCESS".equalsIgnoreCase(status);
    }

    /**
     * Check if the payment failed.
     */
    public boolean isFailed() {
        return "FAILED".equalsIgnoreCase(status);
    }
}

