package org.example.ecommercebackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.ecommercebackend.model.Order;
import org.example.ecommercebackend.model.enums.OrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Response DTO for order data.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {

    private String id;
    private String userId;
    private Double totalAmount;
    private OrderStatus status;
    private List<OrderItemResponse> items;
    private PaymentInfo payment;
    private Instant createdAt;
    private Instant updatedAt;

    public OrderResponse() {
    }

    public OrderResponse(String id, String userId, Double totalAmount, OrderStatus status,
                         List<OrderItemResponse> items, PaymentInfo payment,
                         Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
        this.payment = payment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Factory method to create OrderResponse from Order entity.
     */
    public static OrderResponse fromEntity(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(OrderItemResponse::fromEntity)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus(),
                itemResponses,
                null,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    /**
     * Factory method to create OrderResponse with payment info.
     */
    public static OrderResponse fromEntityWithPayment(Order order, PaymentInfo payment) {
        OrderResponse response = fromEntity(order);
        response.setPayment(payment);
        return response;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    public PaymentInfo getPayment() {
        return payment;
    }

    public void setPayment(PaymentInfo payment) {
        this.payment = payment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Nested class for payment information in order response.
     */
    public static class PaymentInfo {
        private String id;
        private String paymentId;
        private String status;
        private Double amount;
        private Instant createdAt;

        public PaymentInfo() {
        }

        public PaymentInfo(String id, String paymentId, String status, Double amount, Instant createdAt) {
            this.id = id;
            this.paymentId = paymentId;
            this.status = status;
            this.amount = amount;
            this.createdAt = createdAt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Instant getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
        }
    }
}
