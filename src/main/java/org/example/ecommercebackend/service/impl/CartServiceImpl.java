package org.example.ecommercebackend.service.impl;

import org.example.ecommercebackend.dto.request.AddToCartRequest;
import org.example.ecommercebackend.dto.response.CartItemResponse;
import org.example.ecommercebackend.exception.InsufficientStockException;
import org.example.ecommercebackend.exception.ResourceNotFoundException;
import org.example.ecommercebackend.model.CartItem;
import org.example.ecommercebackend.model.Product;
import org.example.ecommercebackend.repository.CartItemRepository;
import org.example.ecommercebackend.repository.ProductRepository;
import org.example.ecommercebackend.service.CartService;
import org.example.ecommercebackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of CartService.
 * Handles business logic for cart operations.
 */
@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public CartServiceImpl(CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           UserService userService) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Override
    public CartItemResponse addToCart(AddToCartRequest request) {
        logger.info("Adding to cart - userId: {}, productId: {}, quantity: {}",
                request.getUserId(), request.getProductId(), request.getQuantity());

        // Validate product exists
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        // Ensure user exists (create if not)
        userService.getOrCreateUser(request.getUserId());

        // Check existing cart item
        Optional<CartItem> existingCartItem = cartItemRepository
                .findByUserIdAndProductId(request.getUserId(), request.getProductId());

        int totalQuantity = request.getQuantity();
        if (existingCartItem.isPresent()) {
            totalQuantity += existingCartItem.get().getQuantity();
        }

        // Validate stock availability
        if (!product.hasStock(totalQuantity)) {
            throw new InsufficientStockException(
                    product.getId(),
                    product.getName(),
                    totalQuantity,
                    product.getStock()
            );
        }

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            // Update existing cart item
            cartItem = existingCartItem.get();
            cartItem.setQuantity(totalQuantity);
            cartItem.setUpdatedAt(Instant.now());
            logger.debug("Updating existing cart item, new quantity: {}", totalQuantity);
        } else {
            // Create new cart item
            cartItem = new CartItem(request.getUserId(), request.getProductId(), request.getQuantity());
            cartItem.setCreatedAt(Instant.now());
            cartItem.setUpdatedAt(Instant.now());
            logger.debug("Creating new cart item");
        }

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        logger.info("Cart item saved successfully with ID: {}", savedCartItem.getId());

        // Build response with product info
        CartItemResponse.ProductInfo productInfo = new CartItemResponse.ProductInfo(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );

        return CartItemResponse.fromEntityWithProduct(savedCartItem, productInfo);
    }

    @Override
    public List<CartItemResponse> getCartByUserId(String userId) {
        logger.debug("Fetching cart for user: {}", userId);

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            return List.of();
        }

        // Fetch all products in one query for efficiency
        List<String> productIds = cartItems.stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        Map<String, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // Build responses with product info
        return cartItems.stream()
                .map(cartItem -> {
                    Product product = productMap.get(cartItem.getProductId());
                    CartItemResponse.ProductInfo productInfo = null;
                    if (product != null) {
                        productInfo = new CartItemResponse.ProductInfo(
                                product.getId(),
                                product.getName(),
                                product.getPrice(),
                                product.getStock()
                        );
                    }
                    return CartItemResponse.fromEntityWithProduct(cartItem, productInfo);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CartItem> getCartItemsByUserId(String userId) {
        logger.debug("Fetching cart items as entities for user: {}", userId);
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public void clearCart(String userId) {
        logger.info("Clearing cart for user: {}", userId);
        cartItemRepository.deleteByUserId(userId);
        logger.info("Cart cleared successfully for user: {}", userId);
    }

    @Override
    public void removeCartItem(String userId, String productId) {
        logger.info("Removing cart item - userId: {}, productId: {}", userId, productId);

        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart item not found for user: " + userId + " and product: " + productId));

        cartItemRepository.delete(cartItem);
        logger.info("Cart item removed successfully");
    }
}
