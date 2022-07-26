package io.alliance.adn.semantic;

import io.alliance.adn.Walkable;

enum TokenKind {
    MISC_END_OF_FILE,
    MISC_BAD_TOKEN,

    KEYWORD_STRUCT,
    KEYWORD_ARRAY,
    KEYWORD_BOOL,
    KEYWORD_I8,
    KEYWORD_I16,
    KEYWORD_I32,
    KEYWORD_I64,
    KEYWORD_F32,
    KEYWORD_F64,
    KEYWORD_STR,

    TOKEN_BRACE_OPEN,
    TOKEN_BRACE_CLOSE,
    TOKEN_BRACKET_OPEN,
    TOKEN_BRACKET_CLOSE,
    TOKEN_EQUAL,
    TOKEN_COMMA,
    TOKEN_SEMICOLON,
    TOKEN_IDENTIFIER,

    LITERAL_TRUE,
    LITERAL_FALSE,
    LITERAL_NUMBER,
    LITERAL_FLOAT,
    LITERAL_STRING
}

public interface Token {

    TokenKind getKind();
    boolean isLiteral();
    boolean isStructural();
    boolean isLiteral();
}

interface Element {

}

class ElementArray implements Element {

}

class ElementStruct implements Element {

}

class ElementRoot extends ElementStruct {

}

class ElementLiteral implements Element {

}

class ElementBool extends ElementLiteral {

}

class Parser extends Walkable<Token> {

    private Token match(TokenKind kind) {
        if(current().getKind() == kind) {
            return next();
        }

        throw new Exception("Nah bro");
    }

    public Parser(Token[] input) {
        super(input);
    }

    public ElementRoot parse() {
        final ElementRoot root = new ElementRoot();

        while (current().getKind() != TokenKind.MISC_END_OF_FILE) {
            root.add(parseElement());
        }

        return root;
    }

    public ElementLiteral parseLiteral() {
        TokenIdentifier identifier = match()
    }
}