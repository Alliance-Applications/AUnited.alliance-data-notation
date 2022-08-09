package io.alliance.adn.api;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() { }

    public InvalidTokenException(String message) {
        super(message);
    }
}