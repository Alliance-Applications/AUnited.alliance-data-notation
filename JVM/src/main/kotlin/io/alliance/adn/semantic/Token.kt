package io.alliance.adn.semantic

internal interface Token {
    val kind: TokenKind

    infix fun kindOf(kind: TokenKind): Boolean {
        return this.kind == kind
    }

    override fun toString(): String
}