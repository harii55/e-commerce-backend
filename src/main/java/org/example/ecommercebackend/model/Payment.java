package org.example.ecommercebackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommercebackend.model.enums.PaymentStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Payment entity representing a payment transaction in the system.
 * Stored in MongoDB 'payments' collection.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    @Indexed
    private String orderId;

    private BigDecimal amount;

    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Indexed(unique = true, sparse = true)
    private String razorpayPaymentId;

    private String razorpayOrderId;

    private String failureReason;

    @Builder.Default
    private Instant createdAt = Instant.now();

    private Instant updatedAt;

    /**
     * Mark the payment as successful.
     */
    public void markAsSuccess(String razorpayPaymentId) {
        this.status = PaymentStatus.SUCCESS;
        this.razorpayPaymentId = razorpayPaymentId;
        this.updatedAt = Instant.now();
    }

    /**
     * Mark the payment as failed.
     */
    public void markAsFailed(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = Instant.now();
    }

    /**
     * Check if payment is in pending state.
     */
    public boolean isPending() {
        return this.status == PaymentStatus.PENDING;
    }

    /**
     * Check if payment was successful.
     */
    public boolean isSuccessful() {
        return this.status == PaymentStatus.SUCCESS;
    }
}

