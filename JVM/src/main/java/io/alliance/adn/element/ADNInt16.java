package io.alliance.adn.element;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@AllArgsConstructor
public final class ADNInt16 implements ADNElement {

    @NotNull
    private final String name;

    private final Short value;

    @Override
    public @NotNull ADNType getType() {
        return ADNType.INT16;
    }
}