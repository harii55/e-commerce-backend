package org.example.ecommercebackend.repository;

import org.example.ecommercebackend.model.Order;
import org.example.ecommercebackend.model.enums.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Order entity.
 * Provides CRUD operations and custom queries for orders.
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    /**
     * Find all orders for a user.
     */
    List<Order> findByUserId(String userId);

    /**
     * Find all orders for a user ordered by creation date descending.
     */
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);

    /**
     * Find orders by status.
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Find orders for a user with a specific status.
     */
    List<Order> findByUserIdAndStatus(String userId, OrderStatus status);

    /**
     * Count orders for a user.
     */
    long countByUserId(String userId);
}
