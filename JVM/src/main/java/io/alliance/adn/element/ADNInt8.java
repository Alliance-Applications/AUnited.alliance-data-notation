package io.alliance.adn.element;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@AllArgsConstructor
public final class ADNInt8 implements ADNElement {

    @NotNull
    private final String name;

    private final Byte value;

    @Override
    public @NotNull ADNType getType() {
        return ADNType.INT8;
    }
}