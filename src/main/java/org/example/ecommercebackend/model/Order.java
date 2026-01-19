package org.example.ecommercebackend.model;

import org.example.ecommercebackend.model.enums.OrderStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Order entity representing a customer's order.
 */
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @Indexed
    private String userId;

    private Double totalAmount;

    private OrderStatus status;

    private List<OrderItem> items = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public Order() {
        this.status = OrderStatus.CREATED;
    }

    public Order(String userId, Double totalAmount) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.CREATED;
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

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
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
     * Add an item to the order.
     */
    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    /**
     * Check if order is in CREATED status (eligible for payment).
     */
    public boolean isPayable() {
        return this.status == OrderStatus.CREATED;
    }

    /**
     * Check if order can be cancelled.
     */
    public boolean isCancellable() {
        return this.status == OrderStatus.CREATED;
    }

    /**
     * Mark the order as paid.
     */
    public void markAsPaid() {
        this.status = OrderStatus.PAID;
        this.updatedAt = Instant.now();
    }

    /**
     * Mark the order as failed.
     */
    public void markAsFailed() {
        this.status = OrderStatus.FAILED;
        this.updatedAt = Instant.now();
    }

    /**
     * Mark the order as cancelled.
     */
    public void markAsCancelled() {
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }
}
