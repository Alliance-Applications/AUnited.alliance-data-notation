package io.alliance.adn.element;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ADNElement {

    @Nullable
    static Object valueOf(@Nullable ADNElement element) {
        return element != null ? element.getValue() : null;
    }

    @Nullable
    static String nameOf(@Nullable ADNElement element) {
        return element != null ? element.getName() : null;
    }

    @Nullable
    static ADNType typeOf(@Nullable ADNElement element) {
        return element != null ? element.getType() : null;
    }

    default boolean typeOf(@NotNull ADNType type) {
        return getType() == type;
    }

    @NotNull
    String getName();

    @NotNull
    ADNType getType();

    @Nullable
    Object getValue();

    @NotNull
    @Override
    String toString();
}