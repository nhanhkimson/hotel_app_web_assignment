package org.example.hotel_mangement.exception;

public class MissingTokenException extends ApiException{
    public MissingTokenException(String message) {
        super("MISSING_TOKEN", message, 401);
    }
}
