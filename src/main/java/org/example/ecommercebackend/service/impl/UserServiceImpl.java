package org.example.ecommercebackend.service.impl;

import org.example.ecommercebackend.model.User;
import org.example.ecommercebackend.repository.UserRepository;
import org.example.ecommercebackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

/**
 * Implementation of UserService.
 * Handles basic user lookup and creation.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getOrCreateUser(String userId) {
        logger.debug("Getting or creating user with ID: {}", userId);

        return userRepository.findById(userId)
                .orElseGet(() -> {
                    logger.info("User not found, creating placeholder user with ID: {}", userId);
                    User newUser = new User();
                    newUser.setId(userId);
                    newUser.setUsername("user_" + userId);
                    newUser.setEmail(userId + "@placeholder.com");
                    newUser.setRole("CUSTOMER");
                    newUser.setCreatedAt(Instant.now());
                    newUser.setUpdatedAt(Instant.now());
                    return userRepository.save(newUser);
                });
    }

    @Override
    public Optional<User> findById(String userId) {
        logger.debug("Finding user by ID: {}", userId);
        return userRepository.findById(userId);
    }

    @Override
    public boolean existsById(String userId) {
        return userRepository.existsById(userId);
    }
}
