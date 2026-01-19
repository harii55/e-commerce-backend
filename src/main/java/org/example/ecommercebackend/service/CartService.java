package org.example.ecommercebackend.service;

import org.example.ecommercebackend.dto.request.AddToCartRequest;
import org.example.ecommercebackend.dto.response.CartItemResponse;
import org.example.ecommercebackend.model.CartItem;

import java.util.List;

/**
 * Service interface for Cart operations.
 */
public interface CartService {

    /**
     * Add an item to the cart.
     * If item already exists, updates the quantity.
     */
    CartItemResponse addToCart(AddToCartRequest request);

    /**
     * Get all cart items for a user with product details.
     */
    List<CartItemResponse> getCartByUserId(String userId);

    /**
     * Get cart items as entities (for internal use).
     */
    List<CartItem> getCartItemsByUserId(String userId);

    /**
     * Clear all items from a user's cart.
     */
    void clearCart(String userId);

    /**
     * Remove a specific item from the cart.
     */
    void removeCartItem(String userId, String productId);
}
