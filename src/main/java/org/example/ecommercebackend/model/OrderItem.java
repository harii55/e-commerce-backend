package org.example.ecommercebackend.model;

/**
 * OrderItem entity representing a single item in an order.
 * Embedded within the Order document.
 */
public class OrderItem {

    private String productId;

    private String productName;

    private Integer quantity;

    private Double price;  // Price at the time of order

    public OrderItem() {
    }

    public OrderItem(String productId, String productName, Integer quantity, Double price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
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

    /**
     * Calculate the subtotal for this item.
     */
    public Double getSubtotal() {
        return this.price * this.quantity;
    }
}
