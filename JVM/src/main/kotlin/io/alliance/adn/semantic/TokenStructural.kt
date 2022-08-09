package io.alliance.adn.semantic

import io.alliance.adn.api.InvalidTokenException
import io.alliance.adn.semantic.TokenKind.*

internal class TokenStructural(override val kind: TokenKind) : Token {

    override fun toString(): String {
        return when (kind) {
            MISC_END_OF_FILE -> "\\EOF"
            MISC_BAD_TOKEN -> "<unknown>"
            TOKEN_BRACE_OPEN -> "{"
            TOKEN_BRACE_CLOSE -> "}"
            TOKEN_BRACKET_OPEN -> "["
            TOKEN_BRACKET_CLOSE -> "]"
            TOKEN_SET -> "="
            TOKEN_COMMA -> ","
            TOKEN_COLON -> ":"
            TOKEN_SEMICOLON -> ";"
            KEYWORD_ARRAY -> "[]"
            KEYWORD_STRUCT -> "struct"
            else -> throw InvalidTokenException("Unexpected token $kind, is not a (known) structural token!")
        }
    }

}