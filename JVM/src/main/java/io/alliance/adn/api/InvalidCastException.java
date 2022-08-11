package io.alliance.adn.api;

import io.alliance.adn.api.kotlin.DataType;
import org.jetbrains.annotations.NotNull;

public class InvalidCastException extends RuntimeException {

    @NotNull
    private final String elementName;

    @NotNull
    private final DataType actualType;

    @NotNull
    private final DataType expectedType;

    public InvalidCastException(@NotNull String elementName, @NotNull DataType actualType, @NotNull DataType expectedType) {
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