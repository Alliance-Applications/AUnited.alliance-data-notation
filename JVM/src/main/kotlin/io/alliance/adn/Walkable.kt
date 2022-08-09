package io.alliance.adn

internal abstract class Walkable<T> protected constructor(
    @JvmField protected val input: List<T>,
    @JvmField protected var index: Int = 0
) {
    protected val size: Int get() = input.size

    protected val current: T get() = input[index]

    protected val next: T get() = input[index + 1]

    protected fun peek(offset: Int): T {
        return input[index + offset]
    }

    protected val consume: T get() {
        val result = input[index]
        index++
        return result
    }

    protected fun skip() {
        index++
    }
}