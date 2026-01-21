package org.example.ecommercebackend.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for creating an order from cart.
 */
public class CreateOrderRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
