package io.alliance.adn.exception;

import io.alliance.adn.element.ADNElement;
import io.alliance.adn.element.ADNType;
import org.jetbrains.annotations.NotNull;

public class InvalidCastException extends RuntimeException {

    @NotNull
    private final String elementName;

    @NotNull
    private final ADNType actualType;

    @NotNull
    private final ADNType expectedType;

    public InvalidCastException(@NotNull ADNElement element, @NotNull ADNType expectedType) {
        elementName = element.getName();
        actualType = element.getType();
        this.expectedType = expectedType;
    }

    public InvalidCastException(@NotNull String elementName, @NotNull ADNType actualType, @NotNull ADNType expectedType) {
        this.elementName = elementName;
        this.actualType = actualType;
        this.expectedType = expectedType;
    }

    @Override
    public String getMessage() {
        return toString();
    }

    @Override
    public String toString() {
        return String.format("Invalid cast of element '%s'(%s) to %s", elementName, actualType, expectedType);
    }
}