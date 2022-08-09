package io.alliance.adn.api;

public class InvalidWriteException extends RuntimeException {

    public InvalidWriteException() { }

    public InvalidWriteException(String message) {
        super(message);
    }
}