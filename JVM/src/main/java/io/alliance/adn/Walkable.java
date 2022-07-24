package main.java.io.alliance.adn;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Walkable<T> {
    protected final T[] input;
    protected int index = 0;

    protected T current() {
        return input[index];
    }

    protected T next() {
        return input[index + 1];
    }

    protected T peek(int offset) {
        return input[index + offset];
    }

    protected T consume() {
        final T result = input[index];
        index++;
        return result;
    }

    protected void skip() {
        index++;
    }
}