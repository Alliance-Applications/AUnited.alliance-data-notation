package io.alliance.adn.lexer;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LexerTest {
    private static String plainADN;
    private static String formattedADN;

    @BeforeAll
    static void insertTestData() {
        plainADN = "object testContainer={int64 id=0;boolean isTestObject=false;string name=\"test container\\\\\";object testObject={int64 id=1;boolean isTestObject=true;string name=\"test object\\\\\";object subObject=null;};};";

        formattedADN = "object testContainer = {\n" +
                       "\tint64 id = 0;\n" +
                       "\tboolean isTestObject = false;\n" +
                       "\tstring name = \"test container\\\\\";\n" +
                       "\tobject testObject = {\n" +
                       "\t\tint64 id = 1;\n" +
                       "\t\tboolean isTestObject = true;\n" +
                       "\t\tstring name = \"test object\\\\\";\n" +
                       "\t\tobject subObject = null;\n" +
                       "\t};\n" +
                       "};";
    }

    @Test
    public void given_validPlainInputString_then_lexerLexesTokens() {
        Lexer lexer = new Lexer(plainADN);
        val tokens = lexer.lex();
        assertNotNull(tokens);
        assertEquals(5, tokens.size());
    }

    @Test
    public void given_validFormattedInputString_then_lexerLexesTokens() {
        Lexer lexer = new Lexer(formattedADN);
        val tokens = lexer.lex();
        assertNotNull(tokens);
        assertEquals(5, tokens.size());
    }
}