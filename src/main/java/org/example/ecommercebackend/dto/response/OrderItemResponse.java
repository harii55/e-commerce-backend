package org.example.ecommercebackend.dto.response;

import org.example.ecommercebackend.model.OrderItem;

/**
 * Response DTO for order item data.
 */
public class OrderItemResponse {

    private String productId;
    private String productName;
    private Integer quantity;
    private Double price;
    private Double subtotal;

    public OrderItemResponse() {
    }

    public OrderItemResponse(String productId, String productName, Integer quantity, 
                             Double price, Double subtotal) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }

    /**
     * Factory method to create OrderItemResponse from OrderItem entity.
     */
    public static OrderItemResponse fromEntity(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getSubtotal()
        );
    }

    // Getters and Setters

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
