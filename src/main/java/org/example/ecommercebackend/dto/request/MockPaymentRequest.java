package org.example.ecommercebackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for mock payment gateway.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MockPaymentRequest {

    private String razorpayOrderId;
    private BigDecimal amount;
    private String currency;
    private String callbackUrl;

    @Builder.Default
    private String currency_default = "INR";

    public static MockPaymentRequest of(String razorpayOrderId, BigDecimal amount, String callbackUrl) {
        return MockPaymentRequest.builder()
                .razorpayOrderId(razorpayOrderId)
                .amount(amount)
                .currency("INR")
                .callbackUrl(callbackUrl)
                .build();
    }
}

