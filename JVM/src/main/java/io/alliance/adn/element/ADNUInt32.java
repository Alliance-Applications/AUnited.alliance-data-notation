package main.java.io.alliance.adn.element;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class ADNUInt32 implements ADNElement {

    @Getter
    @NotNull
    private final String name;

    private final int value;

    public ADNUInt32(@NotNull String name, Long value) {
        this.name = name;
        this.value = value != null ? (int) (value & 0xFFFFFFFF) : 0x00;
    }

    @Override
    public @NotNull ADNType getType() {
        return ADNType.INT32;
    }

    @Override
    public @NotNull Long getValue() {
        return Integer.toUnsignedLong(value);
    }

    @Override
    public @NotNull String toString() {
        return Long.toUnsignedString(value);
    }

    @Override
    public String textify(StringBuilder builder, int indent) {
        return builder.append(getIndent(indent)).append("uint32 ").append(name).append(" = ").append(value).append(";\n").toString();
    }
}