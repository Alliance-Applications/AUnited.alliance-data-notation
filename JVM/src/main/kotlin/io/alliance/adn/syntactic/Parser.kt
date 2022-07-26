package io.alliance.adn.syntactic

import io.alliance.adn.api.kotlin.Dataset
import io.alliance.adn.Walkable
import io.alliance.adn.api.InvalidCastException
import io.alliance.adn.api.InvalidTokenException
import io.alliance.adn.api.kotlin.DataNode
import io.alliance.adn.api.kotlin.DataList
import io.alliance.adn.api.kotlin.Datapoint
import io.alliance.adn.api.kotlin.DataStruct
import io.alliance.adn.api.kotlin.DataType
import io.alliance.adn.semantic.Token
import io.alliance.adn.semantic.TokenIdentifier
import io.alliance.adn.semantic.TokenKind
import io.alliance.adn.semantic.TokenKind.*
import io.alliance.adn.semantic.TokenLiteral
import io.alliance.adn.semantic.TokenType

internal class Parser(input: List<Token>) : Walkable<Token>(input) {
    private fun match(kind: TokenKind): Token {
        if (current kindOf kind) {
            return consume
        }

        throw InvalidTokenException("Unexpected token <$current>, expected <$kind>")
    }

    private fun matchIdentifier(): TokenIdentifier {
        if (current kindOf IDENTIFIER) {
            return consume as TokenIdentifier
        }

        throw InvalidTokenException("Unexpected token <$current>, expected primitive type keyword")
    }

    private fun matchType(): TokenType {
        if (current.kind.isPrimitive) {
            return consume as TokenType
        }

        throw InvalidTokenException("Unexpected token <$current>, expected primitive type keyword")
    }

    private fun matchLiteral(): TokenLiteral {
        if (current.kind.isLiteral) {
            return consume as TokenLiteral
        }

        throw InvalidTokenException("Unexpected token <$current>, expected literal value")
    }

    internal fun parse(): Dataset {
        val root = Dataset()

        while (!(current kindOf MISC_END_OF_FILE)) {
            root += parseElement()
        }

        return root
    }

    private fun parseElement(): DataNode {
        // Sort out structs first

        // struct MyStruct {
        if (current kindOf KEYWORD_STRUCT) {
            consume
            return parseStruct(matchIdentifier())
        }

        val identifier = matchIdentifier()

        // MyStruct {
        if (current kindOf TOKEN_BRACE_OPEN) {
            return parseStruct(identifier)
        }

        // Structs are sorted out, now let's deal with types

        // myNumber: u32 = 0
        // MyArray: u32[][] [ [
        if (current kindOf TOKEN_COLON) {
            consume

            val type = matchType()

            var depth = 0
            while (current kindOf KEYWORD_ARRAY) {
                consume
                depth++
            }

            if (depth > 0) {
                return parseArray(identifier, type.asType(), depth)
            }

            return parseLiteral(identifier, type)
        }

        // myNumber 0
        // MyArray [ [

        var depth = 0
        while (peek(depth) kindOf TOKEN_BRACKET_OPEN) {
            depth++
        }

        if (depth > 0) {
            return parseArray(identifier, DataType.NULL, depth)
        }

        return parseLiteral(identifier, null)
    }

    private fun parseStruct(identifier: TokenIdentifier): DataNode {
        match(TOKEN_BRACE_OPEN)

        val struct = DataStruct(identifier.name)
        while (!(current kindOf TOKEN_BRACE_CLOSE)) {
            struct += parseElement()
        }

        match(TOKEN_BRACE_CLOSE)
        return struct
    }

    private fun parseArray(identifier: TokenIdentifier, expectedType: DataType, depth: Int): DataNode {
        // <MyArray> [ [ 2, 9 ], [
        val (actualType, elements) = parseArrayElement()

        if (expectedType == DataType.NULL) {
            return mapArray(identifier.name, elements, actualType, -(1 - depth), 0)
        }

        if (expectedType != expectedType * actualType) {
            throw InvalidCastException(identifier.name, actualType, expectedType)
        }

        return mapArray(identifier.name, elements, expectedType, -(1 - depth), 0)
    }

    private fun mapArray(name: String, list: ArrayList<*>, type: DataType, depth: Int, layer: Int): DataList {
        if (depth != layer) {
            val array = DataList(name, type, -(layer - depth))

            for (nestedList in list) {
                array += mapArray(name, nestedList as ArrayList<*>, type, depth, layer + 1)
            }

            return array
        }

        val array = DataList(name, type, 0)

        // parseArrayElement cannot produce a list of anything else on the deepest nesting level.
        @Suppress("UNCHECKED_CAST")
        for (value in list as ArrayList<TokenLiteral>) {
            array += Datapoint.anonymous(value.value, type)
        }

        return array
    }

    private fun parseArrayElement(): Pair<DataType, ArrayList<*>> {
        var type = DataType.NULL

        match(TOKEN_BRACKET_OPEN)

        // 'MyArray [ [ ... ], ... ]'
        if (current kindOf TOKEN_BRACKET_OPEN) {
            val list = ArrayList<ArrayList<*>>()

            while (!(current kindOf TOKEN_BRACKET_CLOSE)) {
                val (t, l) = parseArrayElement()
                type *= t
                list.add(l)

                // comma can be omitted
                if (current kindOf TOKEN_COMMA) {
                    consume
                }
            }

            consume
            return Pair(type, list)
        }

        val list = ArrayList<TokenLiteral>()

        // 'MyArray [ 0, ... ]'
        while (!(current kindOf TOKEN_BRACKET_CLOSE)) {
            val literal = matchLiteral()
            type *= inferType(literal)
            list.add(literal)

            // comma can be omitted
            if (current kindOf TOKEN_COMMA) {
                consume
            }
        }

        consume
        return Pair(type, list)
    }

    private fun parseLiteral(identifier: TokenIdentifier, maybeType: TokenType?): Datapoint<*> {
        // value: u32 = 0;
        // Equals and semicolon may be omitted

        if (current kindOf TOKEN_SET) {
            consume
        }

        val value = matchLiteral()
        val type = maybeType?.kind ?: inferKind(value.kind)

        if (current kindOf TOKEN_SEMICOLON) {
            consume
        }

        return Datapoint.create(identifier, type, value)
    }

    private fun inferKind(kind: TokenKind): TokenKind {
        return when (kind) {
            LITERAL_TRUE, LITERAL_FALSE -> KEYWORD_BOOL
            LITERAL_NUMBER -> KEYWORD_I32
            LITERAL_FLOAT -> KEYWORD_F32
            LITERAL_STRING -> KEYWORD_STR
            else -> throw InvalidTokenException("Unexpected token <$current>, expected literal")
        }
    }

    private fun inferType(token: TokenLiteral): DataType {
        return when (token.kind) {
            LITERAL_TRUE, LITERAL_FALSE -> DataType.BOOL
            LITERAL_NUMBER -> DataType.I32
            LITERAL_FLOAT -> DataType.F32
            LITERAL_STRING -> DataType.STR
            else -> throw InvalidTokenException("Unexpected token <$current>, expected literal")
        }
    }
}