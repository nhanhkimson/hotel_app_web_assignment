package org.example.hotel_mangement.exception;

public class ExpiredTokenException extends ApiException {

    public ExpiredTokenException(String message) {
        super("EXPIRED_TOKEN", message, 401);
    }
}
