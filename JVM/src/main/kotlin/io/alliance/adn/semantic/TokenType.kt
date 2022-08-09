package io.alliance.adn.semantic

import io.alliance.adn.api.InvalidTokenException
import io.alliance.adn.data.ElementType
import io.alliance.adn.semantic.TokenKind.*

internal class TokenType(override val kind: TokenKind) : Token {

    override fun toString(): String {
        return when (this.kind) {
            KEYWORD_BOOL -> "bool"
            KEYWORD_I8 -> "i8"
            KEYWORD_I16 -> "i16"
            KEYWORD_I32 -> "i32"
            KEYWORD_I64 -> "i64"
            KEYWORD_F32 -> "f32"
            KEYWORD_F64 -> "f64"
            KEYWORD_STR -> "str"
            else -> throw InvalidTokenException("Unexpected token $kind, is not a primitive!")
        }
    }

    fun asType(): ElementType {
        return when (this.kind) {
            KEYWORD_BOOL -> ElementType.BOOL
            KEYWORD_I8 -> ElementType.I8
            KEYWORD_I16 -> ElementType.I16
            KEYWORD_I32 -> ElementType.I32
            KEYWORD_I64 -> ElementType.I64
            KEYWORD_F32 -> ElementType.F32
            KEYWORD_F64 -> ElementType.F64
            KEYWORD_STR -> ElementType.STR
            else -> throw InvalidTokenException("Unexpected token $this, is not a primitive!")
        }
    }
}