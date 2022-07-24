package main.java.io.alliance.adn.element;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@AllArgsConstructor
public final class ADNBoolean implements ADNElement {

    @NotNull
    private final String name;

    private final boolean value;

    @Override
    public @NotNull ADNType getType() {
        return ADNType.BOOLEAN;
    }

    @Override
    public @NotNull Boolean getValue() {
        return value;
    }

    @Override
    public String textify(StringBuilder builder, int indent) {
        return builder.append(getIndent(indent)).append("boolean ").append(name).append(" = ").append(value).append(";\n").toString();
    }
}