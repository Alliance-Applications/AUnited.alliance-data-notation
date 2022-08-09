package io.alliance.adn.semantic

import io.alliance.adn.api.InvalidTokenException
import io.alliance.adn.api.kotlin.DataType
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

    fun asType(): DataType {
        return when (this.kind) {
            KEYWORD_BOOL -> DataType.BOOL
            KEYWORD_I8 -> DataType.I8
            KEYWORD_I16 -> DataType.I16
            KEYWORD_I32 -> DataType.I32
            KEYWORD_I64 -> DataType.I64
            KEYWORD_F32 -> DataType.F32
            KEYWORD_F64 -> DataType.F64
            KEYWORD_STR -> DataType.STR
            else -> throw InvalidTokenException("Unexpected token $this, is not a primitive!")
        }
    }
}