package org.example.ecommercebackend.service.impl;

import org.example.ecommercebackend.dto.request.CreateOrderRequest;
import org.example.ecommercebackend.dto.response.OrderResponse;
import org.example.ecommercebackend.exception.BadRequestException;
import org.example.ecommercebackend.exception.InsufficientStockException;
import org.example.ecommercebackend.exception.ResourceNotFoundException;
import org.example.ecommercebackend.model.CartItem;
import org.example.ecommercebackend.model.Order;
import org.example.ecommercebackend.model.OrderItem;
import org.example.ecommercebackend.model.Product;
import org.example.ecommercebackend.repository.OrderRepository;
import org.example.ecommercebackend.repository.ProductRepository;
import org.example.ecommercebackend.service.CartService;
import org.example.ecommercebackend.service.OrderService;
import org.example.ecommercebackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of OrderService.
 * Handles business logic for order operations.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            CartService cartService,
                            UserService userService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.userService = userService;
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        logger.info("Creating order for user: {}", request.getUserId());

        // Ensure user exists
        userService.getOrCreateUser(request.getUserId());

        // Get cart items
        List<CartItem> cartItems = cartService.getCartItemsByUserId(request.getUserId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cannot create order: Cart is empty");
        }

        // Fetch all products
        List<String> productIds = cartItems.stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        Map<String, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // Validate stock and build order items
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (CartItem cartItem : cartItems) {
            Product product = productMap.get(cartItem.getProductId());

            if (product == null) {
                throw new ResourceNotFoundException("Product", "id", cartItem.getProductId());
            }

            if (!product.hasStock(cartItem.getQuantity())) {
                throw new InsufficientStockException(
                        product.getId(),
                        product.getName(),
                        cartItem.getQuantity(),
                        product.getStock()
                );
            }

            OrderItem orderItem = new OrderItem(
                    product.getId(),
                    product.getName(),
                    cartItem.getQuantity(),
                    product.getPrice()
            );
            orderItems.add(orderItem);
            totalAmount += orderItem.getSubtotal();
        }

        // Create order
        Order order = new Order(request.getUserId(), totalAmount);
        order.setItems(orderItems);
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());

        // Deduct stock from products
        for (CartItem cartItem : cartItems) {
            Product product = productMap.get(cartItem.getProductId());
            product.reduceStock(cartItem.getQuantity());
            productRepository.save(product);
            logger.debug("Reduced stock for product {}: new stock = {}", 
                    product.getId(), product.getStock());
        }

        // Save order
        Order savedOrder = orderRepository.save(order);
        logger.info("Order created successfully with ID: {}", savedOrder.getId());

        // Clear cart
        cartService.clearCart(request.getUserId());
        logger.info("Cart cleared for user: {}", request.getUserId());

        return OrderResponse.fromEntity(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(String orderId) {
        logger.debug("Fetching order with ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        return OrderResponse.fromEntity(order);
    }

    @Override
    public Order getOrderEntityById(String orderId) {
        logger.debug("Fetching order entity with ID: {}", orderId);

        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(String userId) {
        logger.debug("Fetching orders for user: {}", userId);

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return orders.stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void markOrderAsPaid(String orderId) {
        logger.info("Marking order as PAID: {}", orderId);

        Order order = getOrderEntityById(orderId);
        order.markAsPaid();
        orderRepository.save(order);

        logger.info("Order {} marked as PAID", orderId);
    }

    @Override
    public void markOrderAsFailed(String orderId) {
        logger.info("Marking order as FAILED: {}", orderId);

        Order order = getOrderEntityById(orderId);
        order.markAsFailed();
        orderRepository.save(order);

        logger.info("Order {} marked as FAILED", orderId);
    }
}
