package main.java.io.alliance.adn.element;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@ToString
@AllArgsConstructor
public final class ADNString implements ADNElement {

    @NotNull
    private final String name;

    @Nullable
    private final String value;

    @Override
    public @NotNull ADNType getType() {
        return ADNType.STRING;
    }

    @Override
    public String textify(StringBuilder builder, int indent) {
        return builder.append(getIndent(indent)).append("string ").append(name).append(" = ").append(value).append(";\n").toString();
    }
}