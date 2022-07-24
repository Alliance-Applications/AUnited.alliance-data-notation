package main.java.io.alliance.adn.element;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class ADNUInt8 implements ADNElement {

    @Getter
    @NotNull
    private final String name;

    private final byte value;

    public ADNUInt8(@NotNull String name, Long value) {
        this.name = name;
        this.value = value != null ? (byte) (value & 0xFF) : 0x00;
    }

    @Override
    public @NotNull ADNType getType() {
        return ADNType.INT8;
    }

    @Override
    public @NotNull Long getValue() {
        return Byte.toUnsignedLong(value);
    }

    @Override
    public @NotNull String toString() {
        return Long.toUnsignedString(value);
    }

    @Override
    public String textify(StringBuilder builder, int indent) {
        return builder.append(getIndent(indent)).append("uint8 ").append(name).append(" = ").append(value).append(";\n").toString();
    }
}