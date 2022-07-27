package io.alliance.adn.semantic

import io.alliance.adn.Walkable
import io.alliance.adn.api.InvalidTokenException

class Parser(input: Array<Token>) : Walkable<Token>(input) {
    private fun match(kind: TokenKind): Token {
        if (current().kind == kind) {
            return consume()
        }

        throw InvalidTokenException(
            String.format(
                "Unexpected token <%s>, expected <%s>",
                current().toString(),
                kind.toString()
            )
        )
    }

    private fun matchPrimitive(): Token {
        if (current().kind.isPrimitive) {
            return consume()
        }

        throw InvalidTokenException(
            String.format(
                "Unexpected token <%s>, expected primitive type keyword",
                current().toString()
            )
        )
    }

    private fun matchLiteral(): Token {
        if (current().kind.isLiteral) {
            return consume()
        }

        throw InvalidTokenException(
            String.format(
                "Unexpected token <%s>, expected literal",
                current().toString()
            )
        )
    }

    internal fun parse(): ElementRoot {
        val root = ElementRoot()

        while (current().kind != TokenKind.MISC_END_OF_FILE) {
            root.add(parseElement())
        }

        return root
    }

    private fun parseElement(): Element {
        when (current().kind) {
            TokenKind.IDENTIFIER ->
                when (next().kind) {
                TokenKind.TOKEN_SET -> return parseLiteral()
                TokenKind.TOKEN_BRACKET_OPEN -> return parseArray()
                TokenKind.TOKEN_BRACE_OPEN -> return parseStruct()
                else -> {}
            }

            TokenKind.KEYWORD_STRUCT -> return parseStruct()
            TokenKind.KEYWORD_BOOL, TokenKind.KEYWORD_I8, TokenKind.KEYWORD_I16, TokenKind.KEYWORD_I32, TokenKind.KEYWORD_I64, TokenKind.KEYWORD_F32, TokenKind.KEYWORD_F64, TokenKind.KEYWORD_STR -> return if (next()!!.kind == TokenKind.KEYWORD_ARRAY) parseArray() else parseLiteral()
            else -> {}
        }
        throw InvalidTokenException(String.format("Unexpected token <%s>", current().toString()))
    }

    private fun parseStruct(): Element {
        // TODO
    }

    private fun parseArray(): Element {
        // arr: i32[] [
        // arr [
        val identifier: TokenIdentifier = match(TokenKind.IDENTIFIER) as TokenIdentifier

        if (current().kind == TokenKind.TOKEN_COLON) {
            consume()


        }
        // TODO
    }

    private fun parseLiteral(): ElementLiteral {
        val identifier: TokenIdentifier = match(TokenKind.IDENTIFIER) as TokenIdentifier

        val type = if (current().kind == TokenKind.TOKEN_COLON) {
            // 0 1 2   3 4 5
            // i : i32 = 0 ;
            match(TokenKind.TOKEN_COLON)
            matchPrimitive().kind
        } else {
            // 0 1 2 3
            // i = 0
            inferType(peek(2).kind)
        }

        match(TokenKind.TOKEN_SET)

        val value = matchLiteral()

        if (current().kind == TokenKind.TOKEN_SEMICOLON) {
            match(TokenKind.TOKEN_SEMICOLON)
        }

        return ElementLiteral.create(identifier, type, value)
    }

    private fun inferType(kind: TokenKind): TokenKind {
        return when (kind) {
            TokenKind.LITERAL_TRUE, TokenKind.LITERAL_FALSE -> TokenKind.KEYWORD_BOOL
            TokenKind.LITERAL_NUMBER -> TokenKind.KEYWORD_I32
            TokenKind.LITERAL_FLOAT -> TokenKind.KEYWORD_F32
            TokenKind.LITERAL_STRING -> TokenKind.KEYWORD_STR
            else -> throw InvalidTokenException(String.format("Unexpected token <%s>, expected literal", current().toString()))
        }
    }
}