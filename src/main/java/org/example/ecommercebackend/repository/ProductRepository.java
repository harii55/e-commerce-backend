package org.example.ecommercebackend.repository;

import org.example.ecommercebackend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Product entity.
 * Provides CRUD operations and custom queries for products.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    /**
     * Find products by name containing the search term (case-insensitive).
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Find products with stock greater than zero.
     */
    List<Product> findByStockGreaterThan(Integer stock);

    /**
     * Check if a product with the given name exists.
     */
    boolean existsByName(String name);
}
