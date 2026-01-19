package org.example.ecommercebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommercebackend.model.Payment;
import org.example.ecommercebackend.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response DTO for payment information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private String id;
    private String orderId;
    private BigDecimal amount;
    private PaymentStatus status;
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String failureReason;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Convert Payment entity to PaymentResponse DTO.
     */
    public static PaymentResponse fromEntity(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .razorpayPaymentId(payment.getRazorpayPaymentId())
                .razorpayOrderId(payment.getRazorpayOrderId())
                .failureReason(payment.getFailureReason())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}

