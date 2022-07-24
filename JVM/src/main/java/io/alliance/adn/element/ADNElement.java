package main.java.io.alliance.adn.element;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ADNElement {

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

    String textify(StringBuilder builder, int indent);

    default String getIndent(int indent) {
        StringBuilder tabs = new StringBuilder();

        for(int i = indent; i > 0; i--) {
            tabs.append("\t");
        }

        return tabs.toString();
    }
}