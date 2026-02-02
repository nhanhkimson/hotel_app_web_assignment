package org.example.hotel_mangement.exception;

public class InvalidTokenException extends ApiException {

    public InvalidTokenException(String message) {
        super("INVALID_TOKEN", message, 401);
    }
}
