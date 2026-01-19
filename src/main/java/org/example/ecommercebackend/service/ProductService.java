package org.example.ecommercebackend.service;

import org.example.ecommercebackend.dto.request.CreateProductRequest;
import org.example.ecommercebackend.dto.response.ProductResponse;

import java.util.List;

/**
 * Service interface for Product operations.
 */
public interface ProductService {

    /**
     * Create a new product.
     */
    ProductResponse createProduct(CreateProductRequest request);

    /**
     * Get all products.
     */
    List<ProductResponse> getAllProducts();

    /**
     * Get a product by its ID.
     */
    ProductResponse getProductById(String id);

    /**
     * Search products by name.
     */
    List<ProductResponse> searchProductsByName(String name);
}
