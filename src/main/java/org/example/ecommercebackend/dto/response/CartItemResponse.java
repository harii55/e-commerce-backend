package org.example.ecommercebackend.dto.response;

import org.example.ecommercebackend.model.CartItem;

import java.time.Instant;

/**
 * Response DTO for cart item data.
 */
public class CartItemResponse {

    private String id;
    private String userId;
    private String productId;
    private Integer quantity;
    private ProductInfo product;
    private Instant createdAt;
    private Instant updatedAt;

    public CartItemResponse() {
    }

    public CartItemResponse(String id, String userId, String productId, Integer quantity,
                            ProductInfo product, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.product = product;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Factory method to create CartItemResponse from CartItem entity.
     */
    public static CartItemResponse fromEntity(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getUserId(),
                cartItem.getProductId(),
                cartItem.getQuantity(),
                null,
                cartItem.getCreatedAt(),
                cartItem.getUpdatedAt()
        );
    }

    /**
     * Factory method to create CartItemResponse with product info.
     */
    public static CartItemResponse fromEntityWithProduct(CartItem cartItem, ProductInfo product) {
        CartItemResponse response = fromEntity(cartItem);
        response.setProduct(product);
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public void setProduct(ProductInfo product) {
        this.product = product;
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
     * Nested class for product information in cart response.
     */
    public static class ProductInfo {
        private String id;
        private String name;
        private Double price;
        private Integer stock;

        public ProductInfo() {
        }

        public ProductInfo(String id, String name, Double price, Integer stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getStock() {
            return stock;
        }

        public void setStock(Integer stock) {
            this.stock = stock;
        }
    }
}
