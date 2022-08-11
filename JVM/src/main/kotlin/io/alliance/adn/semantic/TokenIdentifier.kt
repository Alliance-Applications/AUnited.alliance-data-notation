package io.alliance.adn.semantic

import io.alliance.adn.semantic.TokenKind.*

internal class TokenIdentifier(val name: String) : Token {
    override val kind: TokenKind = IDENTIFIER

    override fun toString(): String {
        return "'$name'"
    }

}