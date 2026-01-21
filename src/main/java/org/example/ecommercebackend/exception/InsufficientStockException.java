package org.example.ecommercebackend.exception;

/**
 * Exception thrown when there is insufficient stock for a product.
 * Results in HTTP 400 Bad Request response.
 */
public class InsufficientStockException extends RuntimeException {

    private final String productId;
    private final String productName;
    private final int requestedQuantity;
    private final int availableStock;

    public InsufficientStockException(String productId, String productName, 
                                       int requestedQuantity, int availableStock) {
        super(String.format("Insufficient stock for product '%s'. Requested: %d, Available: %d",
                productName, requestedQuantity, availableStock));
        this.productId = productId;
        this.productName = productName;
        this.requestedQuantity = requestedQuantity;
        this.availableStock = availableStock;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public int getAvailableStock() {
        return availableStock;
    }
}
