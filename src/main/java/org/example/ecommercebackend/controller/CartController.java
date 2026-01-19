package org.example.ecommercebackend.controller;

import jakarta.validation.Valid;
import org.example.ecommercebackend.dto.request.AddToCartRequest;
import org.example.ecommercebackend.dto.response.CartItemResponse;
import org.example.ecommercebackend.dto.response.MessageResponse;
import org.example.ecommercebackend.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Cart operations.
 * Handles HTTP requests for cart management.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Add item to cart.
     * POST /api/cart/add
     */
    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addToCart(
            @Valid @RequestBody AddToCartRequest request) {

        logger.info("Received request to add item to cart for user: {}", request.getUserId());
        CartItemResponse response = cartService.addToCart(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get user's cart.
     * GET /api/cart/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCart(@PathVariable String userId) {
        logger.info("Received request to get cart for user: {}", userId);
        List<CartItemResponse> cartItems = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    /**
     * Clear user's cart.
     * DELETE /api/cart/{userId}/clear
     */
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<MessageResponse> clearCart(@PathVariable String userId) {
        logger.info("Received request to clear cart for user: {}", userId);
        cartService.clearCart(userId);
        return ResponseEntity.ok(MessageResponse.of("Cart cleared successfully"));
    }

    /**
     * Remove specific item from cart.
     * DELETE /api/cart/{userId}/item/{productId}
     */
    @DeleteMapping("/{userId}/item/{productId}")
    public ResponseEntity<MessageResponse> removeCartItem(
            @PathVariable String userId,
            @PathVariable String productId) {

        logger.info("Received request to remove item from cart - userId: {}, productId: {}",
                userId, productId);
        cartService.removeCartItem(userId, productId);
        return ResponseEntity.ok(MessageResponse.of("Item removed from cart successfully"));
    }
}
