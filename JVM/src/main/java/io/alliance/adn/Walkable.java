package io.alliance.adn;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class Walkable<T> {
    @NotNull
    protected final T[] input;
    protected int index = 0;

    @NotNull
    protected T current() {
        return input[index];
    }

    @NotNull
    protected T next() {
        return input[index + 1];
    }

    @NotNull
    protected T peek(int offset) {
        return input[index + offset];
    }

    @NotNull
    protected T consume() {
        final T result = input[index];
        index++;
        return result;
    }

    protected void skip() {
        index++;
    }
}