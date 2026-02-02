package org.example.hotel_mangement.exception;

public class TokenException extends ApiException {
    public TokenException(String message) {
        super("TOKEN_ERROR", message, 401);
    }
}
