package org.example.ecommercebackend.exception;

/**
 * Exception thrown when an operation is attempted on an order in an invalid state.
 * For example, trying to pay for an already paid order.
 * Results in HTTP 400 Bad Request response.
 */
public class InvalidOrderStateException extends RuntimeException {

    private final String orderId;
    private final String currentState;
    private final String expectedState;

    public InvalidOrderStateException(String orderId, String currentState, String expectedState) {
        super(String.format("Order '%s' is in '%s' state. Expected state: '%s'",
                orderId, currentState, expectedState));
        this.orderId = orderId;
        this.currentState = currentState;
        this.expectedState = expectedState;
    }

    public InvalidOrderStateException(String message) {
        super(message);
        this.orderId = null;
        this.currentState = null;
        this.expectedState = null;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCurrentState() {
        return currentState;
    }

    public String getExpectedState() {
        return expectedState;
    }
}
