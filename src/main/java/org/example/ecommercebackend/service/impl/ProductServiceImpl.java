package org.example.ecommercebackend.service.impl;

import org.example.ecommercebackend.dto.request.CreateProductRequest;
import org.example.ecommercebackend.dto.response.ProductResponse;
import org.example.ecommercebackend.exception.ResourceNotFoundException;
import org.example.ecommercebackend.model.Product;
import org.example.ecommercebackend.repository.ProductRepository;
import org.example.ecommercebackend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ProductService.
 * Handles business logic for product operations.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        logger.info("Creating new product: {}", request.getName());

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());

        Product savedProduct = productRepository.save(product);
        logger.info("Product created successfully with ID: {}", savedProduct.getId());

        return ProductResponse.fromEntity(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        logger.debug("Fetching all products");

        List<Product> products = productRepository.findAll();
        
        return products.stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(String id) {
        logger.debug("Fetching product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        return ProductResponse.fromEntity(product);
    }

    @Override
    public List<ProductResponse> searchProductsByName(String name) {
        logger.debug("Searching products with name containing: {}", name);

        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        
        return products.stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
