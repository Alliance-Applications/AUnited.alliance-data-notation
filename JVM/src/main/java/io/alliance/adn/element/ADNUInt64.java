package main.java.io.alliance.adn.element;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class ADNUInt64 implements ADNElement {

    @Getter
    @NotNull
    private final String name;

    private final long value;

    public ADNUInt64(@NotNull String name, Long value) {
        this.name = name;
        this.value = value != null ? value : 0x00;
    }

    @Override
    public @NotNull ADNType getType() {
        return ADNType.INT64;
    }

    @Override
    public @NotNull Long getValue() {
        return value;
    }

    @Override
    public @NotNull String toString() {
        return Long.toUnsignedString(value);
    }

    @Override
    public String textify(StringBuilder builder, int indent) {
        return builder.append(getIndent(indent)).append("uint64 ").append(name).append(" = ").append(value).append(";\n").toString();
    }
}