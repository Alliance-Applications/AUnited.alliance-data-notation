package io.alliance.adn.semantic

import io.alliance.adn.Walkable

internal class Lexer(input: List<Char>) : Walkable<Char>(input) {
    private val builder = StringBuilder()

    internal fun lex(): List<Token> {
        val result = ArrayList<Token>()

        while (index < size) {
            result.add(lexToken())
        }

        result.add(TokenStructural(TokenKind.MISC_END_OF_FILE))
        return result
    }

    private fun lexToken(): Token {
        while (current.isWhitespace()) {
            consume
        }

        when (current) {
            '=' -> {
                index++
                return TokenStructural(TokenKind.TOKEN_SET)
            }
            ',' -> {
                index++
                return TokenStructural(TokenKind.TOKEN_COMMA)
            }
            ':' -> {
                index++
                return TokenStructural(TokenKind.TOKEN_COLON)
            }
            ';' -> {
                index++
                return TokenStructural(TokenKind.TOKEN_SEMICOLON)
            }
            '{' -> {
                index++
                return TokenStructural(TokenKind.TOKEN_BRACE_OPEN)
            }
            '}' -> {
                index++
                return TokenStructural(TokenKind.TOKEN_BRACE_CLOSE)
            }
            '[' -> {
                if (next == ']') {
                    index += 2
                    return TokenStructural(TokenKind.KEYWORD_ARRAY)
                }
                index++
                return TokenStructural(TokenKind.TOKEN_BRACKET_OPEN)
            }
            ']' -> {
                index++
                return TokenStructural(TokenKind.TOKEN_BRACKET_CLOSE)
            }
            '-', '+', '.' -> {
                builder.append(consume)
                return lexNumber()
            }
            '"' ->  {
                index++
                return lexString()
            }
        }

        if (current.isDigit()) {
            return lexNumber()
        }

        if (current.isLetter()) {
            return lexTextual()
        }

        return TokenStructural(TokenKind.MISC_BAD_TOKEN)
    }

    private fun lexNumber(): Token {
        var bad = false
        var dot = false

        while (current.isDigit() || current == '.') {
            if (current == '.') {
                if (dot) {
                    bad = true
                }
                dot = true
            }

            builder.append(consume)
        }

        if (bad) {
            builder.clear()
            return TokenStructural(TokenKind.MISC_BAD_TOKEN)
        }

        val text = builder.toString()
        builder.clear()

        if (dot) {
            return TokenLiteral(TokenKind.LITERAL_FLOAT, text)
        }

        return TokenLiteral(TokenKind.LITERAL_NUMBER, text)
    }

    private fun lexString(): TokenLiteral {
        while (current != '"') {
            if (current == '\\') {
                when (next) {
                    '\\' -> builder.append('\\')
                    '\t' -> builder.append('\t')
                    '\r' -> builder.append('\r')
                    '\n' -> builder.append('\n')
                    '"' -> builder.append('"')
                    else -> {
                        builder.append('\\')
                        continue
                    }
                }

                index += 2
                continue
            }

            builder.append(consume)
        }

        index++

        val text = builder.toString()
        builder.clear()

        return TokenLiteral(TokenKind.LITERAL_STRING, text)
    }

    private fun lexTextual(): Token {
        while (current.isLetterOrDigit() || current == '_') {
            builder.append(consume)
        }

        val text = builder.toString()
        builder.clear()

        return when (text) {
            "struct" -> TokenStructural(TokenKind.KEYWORD_STRUCT)
            "bool" -> TokenType(TokenKind.KEYWORD_BOOL)
            "i8" -> TokenType(TokenKind.KEYWORD_I8)
            "i16" -> TokenType(TokenKind.KEYWORD_I16)
            "i32" -> TokenType(TokenKind.KEYWORD_I32)
            "i64" -> TokenType(TokenKind.KEYWORD_I64)
            "f32" -> TokenType(TokenKind.KEYWORD_F32)
            "f64" -> TokenType(TokenKind.KEYWORD_F64)
            "str" -> TokenType(TokenKind.KEYWORD_STR)

            "true" -> TokenLiteral.TRUE
            "false" -> TokenLiteral.FALSE
            else -> TokenIdentifier(text)
        }
    }
}