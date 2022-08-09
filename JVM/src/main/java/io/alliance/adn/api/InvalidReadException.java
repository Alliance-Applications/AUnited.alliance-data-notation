package io.alliance.adn.api;

public class InvalidReadException extends RuntimeException {

    public InvalidReadException() { }

    public InvalidReadException(String message) {
        super(message);
    }
}