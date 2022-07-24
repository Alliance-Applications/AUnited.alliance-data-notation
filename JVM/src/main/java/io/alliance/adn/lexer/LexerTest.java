package main.java.io.alliance.adn.lexer;

import main.java.io.alliance.adn.exception.ParsingException;
import main.java.io.alliance.adn.parser.Parser;
import lombok.val;

import java.util.List;

public class LexerTest {
    private static final String testADN = "object testContainer = {\n" +
            "\tuint64 id = 0;\n" +
            "\tboolean isTestObject = false;\n" +
            "\tstring name = \"test container\\\\\";\n" +
            "\tobject testObject = {\n" +
            "\t\tuint64 id = 1;\n" +
            "\t\tboolean isTestObject = true;\n" +
            "\t\tstring name = \"test object\\\\\";\n" +
            "\t\tobject subObject = null;\n" +
            "\t};\n" +
            "};";

    public static void main(String[] args) throws ParsingException {
        final Lexer lexer = new Lexer(testADN);
        List<Token> tokenList = lexer.lex();

        for(final Token token : tokenList) {
            System.out.println(token.toString());
        }

        parse(tokenList.toArray(new Token[0]));
    }

    private static void parse(Token[] tokens) throws ParsingException {
        Parser parser = new Parser(tokens);

        val object = parser.parse();

        object.prettyPrint();
    }
}