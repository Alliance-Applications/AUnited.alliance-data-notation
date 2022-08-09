package io.alliance.adn.api;

import io.alliance.adn.data.ElementType;
import org.jetbrains.annotations.NotNull;

public class InvalidCastException extends RuntimeException {

    @NotNull
    private final String elementName;

    @NotNull
    private final ElementType actualType;

    @NotNull
    private final ElementType expectedType;

    public InvalidCastException(@NotNull String elementName, @NotNull ElementType actualType, @NotNull ElementType expectedType) {
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