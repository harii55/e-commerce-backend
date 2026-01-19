package org.example.ecommercebackend.exception;

/**
 * Exception thrown when payment processing fails.
 * Results in HTTP 500 Internal Server Error or 400 Bad Request based on cause.
 */
public class PaymentProcessingException extends RuntimeException {

    private final String orderId;
    private final String paymentId;

    public PaymentProcessingException(String message) {
        super(message);
        this.orderId = null;
        this.paymentId = null;
    }

    public PaymentProcessingException(String message, String orderId) {
        super(message);
        this.orderId = orderId;
        this.paymentId = null;
    }

    public PaymentProcessingException(String message, String orderId, String paymentId) {
        super(message);
        this.orderId = orderId;
        this.paymentId = paymentId;
    }

    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause);
        this.orderId = null;
        this.paymentId = null;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }
}
