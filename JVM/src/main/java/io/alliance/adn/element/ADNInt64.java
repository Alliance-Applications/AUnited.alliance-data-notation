package main.java.io.alliance.adn.element;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@AllArgsConstructor
public final class ADNInt64 implements ADNElement {

    @NotNull
    private final String name;

    private final Long value;

    @Override
    public @NotNull ADNType getType() {
        return ADNType.INT64;
    }

    @Override
    public String textify(StringBuilder builder, int indent) {
        return builder.append(getIndent(indent)).append("int64 ").append(name).append(" = ").append(value).append(";\n").toString();
    }
}