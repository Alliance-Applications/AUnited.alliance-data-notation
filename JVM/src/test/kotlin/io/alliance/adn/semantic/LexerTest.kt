package io.alliance.adn.semantic

import org.junit.Ignore
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LexerTest {
    private lateinit var text: List<Char>

    @BeforeEach
    fun setUp() {
        val input = "struct my_Struct { array: f64[][] [[+1.0, -1.0], [.125, 0]; String = \"test\\\n\\ \" }"
        text = input.toList()
    }

    @Test
    @Ignore
    // ToDo!
    fun givenString_thenLexerShouldReturnTokens() {
        // val result = Lexer(text).lex()
    }
}