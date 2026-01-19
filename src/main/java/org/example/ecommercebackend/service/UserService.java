package org.example.ecommercebackend.service;

import org.example.ecommercebackend.model.User;

import java.util.Optional;

/**
 * Service interface for User operations.
 */
public interface UserService {

    /**
     * Get or create a user by ID.
     * If user doesn't exist, creates a placeholder user.
     */
    User getOrCreateUser(String userId);

    /**
     * Find a user by ID.
     */
    Optional<User> findById(String userId);

    /**
     * Check if a user exists.
     */
    boolean existsById(String userId);
}
