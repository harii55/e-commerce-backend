package org.example.ecommercebackend.dto.response;

import java.util.Map;

/**
 * Generic message response DTO for simple status messages.
 */
public class MessageResponse {

    private String message;

    public MessageResponse() {
    }

    public MessageResponse(String message) {
        this.message = message;
    }

    public static MessageResponse of(String message) {
        return new MessageResponse(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
