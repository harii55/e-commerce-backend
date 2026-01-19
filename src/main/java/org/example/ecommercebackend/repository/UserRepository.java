package org.example.ecommercebackend.repository;

import org.example.ecommercebackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity.
 * Provides CRUD operations and custom queries for users.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Find a user by username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists with the given username.
     */
    boolean existsByUsername(String username);

    /**
     * Check if a user exists with the given email.
     */
    boolean existsByEmail(String email);
}
