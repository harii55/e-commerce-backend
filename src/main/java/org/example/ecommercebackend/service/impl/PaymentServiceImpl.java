package org.example.ecommercebackend.service.impl;

import org.example.ecommercebackend.dto.request.CreatePaymentRequest;
import org.example.ecommercebackend.dto.response.PaymentResponse;
import org.example.ecommercebackend.exception.BadRequestException;
import org.example.ecommercebackend.exception.InvalidOrderStateException;
import org.example.ecommercebackend.exception.ResourceNotFoundException;
import org.example.ecommercebackend.model.Order;
import org.example.ecommercebackend.model.Payment;
import org.example.ecommercebackend.model.enums.OrderStatus;
import org.example.ecommercebackend.repository.PaymentRepository;
import org.example.ecommercebackend.service.OrderService;
import org.example.ecommercebackend.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Implementation of PaymentService.
 * Handles business logic for payment operations.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              @Lazy OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    @Override
    public PaymentResponse initiatePayment(CreatePaymentRequest request) {
        logger.info("Initiating payment for order: {}", request.getOrderId());

        // Get the order
        Order order = orderService.getOrderEntityById(request.getOrderId());

        // Validate order status
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderStateException(
                    order.getId(),
                    order.getStatus().name(),
                    "Order must be in CREATED state to initiate payment"
            );
        }

        // Check if payment already exists for this order
        if (paymentRepository.existsByOrderId(request.getOrderId())) {
            Payment existingPayment = paymentRepository.findByOrderId(request.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment", "orderId", request.getOrderId()));
            
            if (existingPayment.isPending()) {
                logger.info("Returning existing pending payment for order: {}", request.getOrderId());
                return PaymentResponse.fromEntity(existingPayment);
            } else if (existingPayment.isSuccessful()) {
                throw new BadRequestException("Payment already completed for this order");
            }
        }

        // Generate a mock Razorpay order ID
        String razorpayOrderId = "order_" + UUID.randomUUID().toString().replace("-", "").substring(0, 14);

        // Create payment record
        Payment payment = Payment.builder()
                .orderId(order.getId())
                .amount(BigDecimal.valueOf(order.getTotalAmount()))
                .razorpayOrderId(razorpayOrderId)
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Payment created with ID: {} for order: {}", savedPayment.getId(), order.getId());

        return PaymentResponse.fromEntity(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentById(String paymentId) {
        logger.debug("Fetching payment with ID: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        return PaymentResponse.fromEntity(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(String orderId) {
        logger.debug("Fetching payment for order: {}", orderId);

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "orderId", orderId));

        return PaymentResponse.fromEntity(payment);
    }

    @Override
    public Payment getPaymentByRazorpayOrderId(String razorpayOrderId) {
        logger.debug("Fetching payment by Razorpay order ID: {}", razorpayOrderId);

        return paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "razorpayOrderId", razorpayOrderId));
    }

    @Override
    public void processPaymentSuccess(String razorpayOrderId, String razorpayPaymentId) {
        logger.info("Processing payment success for Razorpay order: {}", razorpayOrderId);

        Payment payment = getPaymentByRazorpayOrderId(razorpayOrderId);

        if (!payment.isPending()) {
            logger.warn("Payment {} is not in PENDING state, skipping update", payment.getId());
            return;
        }

        // Update payment status
        payment.markAsSuccess(razorpayPaymentId);
        paymentRepository.save(payment);
        logger.info("Payment {} marked as SUCCESS", payment.getId());

        // Update order status
        orderService.markOrderAsPaid(payment.getOrderId());
    }

    @Override
    public void processPaymentFailure(String razorpayOrderId, String reason) {
        logger.info("Processing payment failure for Razorpay order: {}", razorpayOrderId);

        Payment payment = getPaymentByRazorpayOrderId(razorpayOrderId);

        if (!payment.isPending()) {
            logger.warn("Payment {} is not in PENDING state, skipping update", payment.getId());
            return;
        }

        // Update payment status
        payment.markAsFailed(reason);
        paymentRepository.save(payment);
        logger.info("Payment {} marked as FAILED", payment.getId());

        // Update order status
        orderService.markOrderAsFailed(payment.getOrderId());
    }
}

