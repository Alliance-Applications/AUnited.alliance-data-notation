package io.alliance.adn.semantic;

import org.jetbrains.annotations.NotNull;

enum TokenKind {
    MISC_END_OF_FILE(false, false),
    MISC_BAD_TOKEN(false, false),

    IDENTIFIER(false, false),

    KEYWORD_STRUCT(false, false),
    KEYWORD_ARRAY(false, false),
    KEYWORD_BOOL(true, false),
    KEYWORD_I8(true, false),
    KEYWORD_I16(true, false),
    KEYWORD_I32(true, false),
    KEYWORD_I64(true, false),
    KEYWORD_F32(true, false),
    KEYWORD_F64(true, false),
    KEYWORD_STR(true, false),

    TOKEN_BRACE_OPEN(false, false),
    TOKEN_BRACE_CLOSE(false, false),
    TOKEN_BRACKET_OPEN(false, false),
    TOKEN_BRACKET_CLOSE(false, false),
    TOKEN_SET(false, false),
    TOKEN_COMMA(false, false),
    TOKEN_COLON(false, false),
    TOKEN_SEMICOLON(false, false),

    LITERAL_TRUE(false, true),
    LITERAL_FALSE(false, true),
    LITERAL_NUMBER(false, true),
    LITERAL_FLOAT(false, true),
    LITERAL_STRING(false, true);

    public final boolean isPrimitive;
    public final boolean isLiteral;

    TokenKind(boolean isPrimitive, boolean isLiteral) {
        this.isPrimitive = isPrimitive;
        this.isLiteral = isLiteral;
    }
}

public interface Token {
    @NotNull
    TokenKind getKind();
}

class TokenIdentifier implements Token {

    @Override
    public @NotNull TokenKind getKind() {
        return TokenKind.IDENTIFIER;
    }

}

interface Element {

}

class ElementArray implements Element {

}

class ElementStruct implements Element {

}

class ElementRoot extends ElementStruct {

    public void add(Element element) {

    }
}

class ElementLiteral implements Element {

    @NotNull
    public static ElementLiteral create(@NotNull TokenIdentifier identifier, @NotNull TokenKind type, @NotNull Token value) {

    }
}

class ElementBool extends ElementLiteral {

}