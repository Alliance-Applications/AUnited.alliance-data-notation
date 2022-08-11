package io.alliance.adn.syntactic

import io.alliance.adn.api.kotlin.DataList
import io.alliance.adn.api.kotlin.Datapoint
import io.alliance.adn.api.kotlin.DataStruct
import io.alliance.adn.semantic.*
import io.alliance.adn.semantic.TokenKind.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ParserTest {
    private lateinit var struct: Token
    private lateinit var array: Token

    private lateinit var name: Token
    private lateinit var value: Token

    private lateinit var colon: Token
    private lateinit var comma: Token
    private lateinit var type: Token
    private lateinit var equal: Token
    private lateinit var semicolon: Token
    private lateinit var bracketO: Token
    private lateinit var bracketC: Token
    private lateinit var braceO: Token
    private lateinit var braceC: Token

    private lateinit var eof: Token

    @BeforeEach
    fun setUp() {
        struct = TokenStructural(KEYWORD_STRUCT)
        array =TokenStructural(KEYWORD_ARRAY)

        name = TokenIdentifier("test")
        value = TokenLiteral(LITERAL_NUMBER, "24")

        colon = TokenStructural(TOKEN_COLON)
        comma = TokenStructural(TOKEN_COMMA)
        type = TokenType(KEYWORD_I32)
        equal = TokenStructural(TOKEN_SET)
        semicolon = TokenStructural(TOKEN_SEMICOLON)
        bracketO = TokenStructural(TOKEN_BRACKET_OPEN)
        bracketC = TokenStructural(TOKEN_BRACKET_CLOSE)
        braceO = TokenStructural(TOKEN_BRACE_OPEN)
        braceC = TokenStructural(TOKEN_BRACE_CLOSE)

        eof = TokenStructural(MISC_END_OF_FILE)
    }

    @Test
    fun givenLiteral_thenShouldReturnElementLiteral() {
        // test: i32 = 24;
        run {
            val parser = Parser(listOf(name, colon, type, equal, value, semicolon, eof))
            val result = parser.parse()["test"] as Datapoint<*>
            Assertions.assertEquals(24, result.value)
        }

        // test = 24;
        run {
            val parser = Parser(listOf(name, equal, value, semicolon, eof))
            val result = parser.parse()["test"] as Datapoint<*>
            Assertions.assertEquals(24, result.value)
        }

        // test: i32 24;
        run {
            val parser = Parser(listOf(name, colon, type, value, semicolon, eof))
            val result = parser.parse()["test"] as Datapoint<*>
            Assertions.assertEquals(24, result.value)
        }

        // test: i32 = 24
        run {
            val parser = Parser(listOf(name, colon, type, equal, value, eof))
            val result = parser.parse()["test"] as Datapoint<*>
            Assertions.assertEquals(24, result.value)
        }

        // test 24;
        run {
            val parser = Parser(listOf(name, value, semicolon, eof))
            val result = parser.parse()["test"] as Datapoint<*>
            Assertions.assertEquals(24, result.value)
        }

        // test 24
        run {
            val parser = Parser(listOf(name, value, eof))
            val result = parser.parse()["test"] as Datapoint<*>
            Assertions.assertEquals(24, result.value)
        }
    }

    @Test
    fun givenArray_thenShouldReturnElementArray() {
        // test: i32[][] [[24, 24], [24, 24]]
        kotlin.run {
            val parser = Parser(listOf(name, colon, type, array, array, bracketO, bracketO, value, comma, value, bracketC, comma, bracketO, value, comma, value, bracketC, bracketC, eof))
            val result = parser.parse()["test"] as DataList
            Assertions.assertEquals(24, (result[0, 0] as Datapoint<*>).value)
            Assertions.assertEquals(24, (result[0, 1] as Datapoint<*>).value)
            Assertions.assertEquals(24, (result[1, 0] as Datapoint<*>).value)
            Assertions.assertEquals(24, (result[1, 1] as Datapoint<*>).value)
        }

        // test [[+1.0, -1.0], [.125, 0]]
        kotlin.run {
            // Combination of I32 and F32 results in F64 per specification!
            val value00 = TokenLiteral(LITERAL_FLOAT, "+1.0")
            val value01 = TokenLiteral(LITERAL_FLOAT, "-1.0")
            val value10 = TokenLiteral(LITERAL_FLOAT, ".125")
            val value11 = TokenLiteral(LITERAL_NUMBER, "0")

            val parser = Parser(listOf(name, bracketO, bracketO, value00, comma, value01, bracketC, comma, bracketO, value10, comma, value11, bracketC, bracketC, eof))
            val result = parser.parse()["test"] as DataList

            Assertions.assertEquals(1.0, (result[0, 0] as Datapoint<*>).value)
            Assertions.assertEquals(-1.0, (result[0, 1] as Datapoint<*>).value)
            Assertions.assertEquals(.125, (result[1, 0] as Datapoint<*>).value)
            Assertions.assertEquals(0, (result[1, 1] as Datapoint<*>).value)
        }

        // test: i32[] [24, 24]
        kotlin.run {
            val parser = Parser(listOf(name, colon, type, array, bracketO, value, comma, value, bracketC, eof))
            val result = parser.parse()["test"] as DataList
            Assertions.assertEquals(24, (result[0] as Datapoint<*>).value)
            Assertions.assertEquals(24, (result[1] as Datapoint<*>).value)
        }

        // test [24, 24]
        kotlin.run {
            val parser = Parser(listOf(name, bracketO, value, comma, value, bracketC, eof))
            val result = parser.parse()["test"] as DataList
            Assertions.assertEquals(24, (result[0] as Datapoint<*>).value)
            Assertions.assertEquals(24, (result[1] as Datapoint<*>).value)
        }
    }

    @Test
    fun givenStruct_thenShouldReturnElementStruct() {
        // struct test { test 24 }
        run {
            val parser = Parser(listOf(struct, name, braceO, name, value, braceC, eof))
            val result = parser.parse()["test"] as DataStruct
            Assertions.assertEquals(24, (result["test"] as Datapoint<*>).value)
        }

        // struct test { test [24, 24] }
        kotlin.run {
            val parser = Parser(listOf(struct, name, braceO, name, bracketO, value, comma, value, bracketC, braceC, eof))
            val result = parser.parse()["test"] as DataStruct
            Assertions.assertEquals(24, ((result["test"] as DataList)[0] as Datapoint<*>).value)
            Assertions.assertEquals(24, ((result["test"] as DataList)[1] as Datapoint<*>).value)
        }

        // struct test { struct test { test 24 } }
        run {
            val parser = Parser(listOf(struct, name, braceO, struct, name, braceO, name, value, braceC, braceC, eof))
            val result = parser.parse()["test"] as DataStruct
            Assertions.assertEquals(24, (result["test", "test"] as Datapoint<*>).value)
        }
    }
}