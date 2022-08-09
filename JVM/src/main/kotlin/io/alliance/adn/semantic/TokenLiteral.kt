package io.alliance.adn.semantic

import io.alliance.adn.api.InvalidTokenException
import io.alliance.adn.semantic.TokenKind.*

internal class TokenLiteral(override val kind: TokenKind, val value: String) : Token {

    override fun toString(): String {
        return when (kind) {
            LITERAL_TRUE -> "true (boolean)"
            LITERAL_FALSE -> "false (boolean)"
            LITERAL_NUMBER -> "$value (number)"
            LITERAL_FLOAT -> "$value (floating point)"
            LITERAL_STRING -> "$value (string)"
            else -> throw InvalidTokenException("Unexpected token $kind, is not a literal!")
        }
    }

}