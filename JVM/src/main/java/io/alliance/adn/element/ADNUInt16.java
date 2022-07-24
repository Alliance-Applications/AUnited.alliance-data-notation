package main.java.io.alliance.adn.element;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class ADNUInt16 implements ADNElement {

    @Getter
    @NotNull
    private final String name;

    private final char value;

    public ADNUInt16(@NotNull String name, Long value) {
        this.name = name;
        this.value = value != null ? (char) (value & 0xFFFF) : 0x00;
    }

    @Override
    public @NotNull ADNType getType() {
        return ADNType.INT16;
    }

    @Override
    public @NotNull Long getValue() {
        return Integer.toUnsignedLong(Character.getNumericValue(value));
    }

    @Override
    public @NotNull String toString() {
        return Long.toUnsignedString(value);
    }

    @Override
    public String textify(StringBuilder builder, int indent) {
        return builder.append(getIndent(indent)).append("uint16 ").append(name).append(" = ").append(value).append(";\n").toString();
    }
}