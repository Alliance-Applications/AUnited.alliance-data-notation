package main.java.io.alliance.adn.element;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@AllArgsConstructor
public final class ADNInt32 implements ADNElement {

    @NotNull
    private final String name;

    private final Integer value;

    @Override
    public @NotNull ADNType getType() {
        return ADNType.INT32;
    }

    @Override
    public String textify(StringBuilder builder, int indent) {
        return builder.append(getIndent(indent)).append("int32 ").append(name).append(" = ").append(value).append(";\n").toString();
    }
}