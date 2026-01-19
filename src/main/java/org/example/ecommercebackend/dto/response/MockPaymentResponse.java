package org.example.ecommercebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO from mock payment gateway.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MockPaymentResponse {

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String status;
    private String message;

    public static MockPaymentResponse success(String razorpayOrderId, String razorpayPaymentId) {
        return MockPaymentResponse.builder()
                .razorpayOrderId(razorpayOrderId)
                .razorpayPaymentId(razorpayPaymentId)
                .status("SUCCESS")
                .message("Payment processed successfully")
                .build();
    }

    public static MockPaymentResponse failure(String razorpayOrderId, String reason) {
        return MockPaymentResponse.builder()
                .razorpayOrderId(razorpayOrderId)
                .status("FAILED")
                .message(reason)
                .build();
    }
}

