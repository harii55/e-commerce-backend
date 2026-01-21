package org.example.ecommercebackend.repository;

import org.example.ecommercebackend.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CartItem entity.
 * Provides CRUD operations and custom queries for cart items.
 */
@Repository
public interface CartItemRepository extends MongoRepository<CartItem, String> {

    /**
     * Find all cart items for a user.
     */
    List<CartItem> findByUserId(String userId);

    /**
     * Find a specific cart item by user and product.
     */
    Optional<CartItem> findByUserIdAndProductId(String userId, String productId);

    /**
     * Delete all cart items for a user.
     */
    void deleteByUserId(String userId);

    /**
     * Count cart items for a user.
     */
    long countByUserId(String userId);

    /**
     * Check if a cart item exists for user and product.
     */
    boolean existsByUserIdAndProductId(String userId, String productId);
}
