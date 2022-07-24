package main.java.io.alliance.adn.lexer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum TokenType {
    IDENTIFIER(null),
    VALUE_STRING(null),
    VALUE_NUMERIC(null),
    VALUE_NULL("null"),
    VALUE_TRUE("true"),
    VALUE_FALSE("false"),
    VALUE_EOF("\0"),

    BRACE_OPEN("{"),
    BRACE_CLOSE("}"),
    SQUARE_OPEN("["),
    SQUARE_CLOSE("]"),
    COMMA(","),
    EQUALS("="),
    SEMICOLON(";"),

    BOOLEAN("boolean"),
    INT8("int8"),
    INT16("int16"),
    INT32("int32"),
    INT64("int64"),
    UINT8("uint8"),
    UINT16("uint16"),
    UINT32("uint32"),
    UINT64("uint64"),
    FP32("fp32"),
    FP64("fp64"),
    STRING("string"),
    OBJECT("object");

    private String definition;

    TokenType(String definition) {
        this.definition = definition;
    }

    @Contract(pure= true)
    public static @Nullable TokenType getSingleCharType(char character) {
        switch (character) {
            // Symbols
            case '{':
                return BRACE_OPEN;
            case '}':
                return BRACE_CLOSE;
            case '[':
                return SQUARE_OPEN;
            case ']':
                return SQUARE_CLOSE;
            case ',':
                return COMMA;
            case '=':
                return EQUALS;
            case ';':
                return SEMICOLON;
            default:
                return null;
        }
    }

    @Contract(pure = true)
    public static @NotNull TokenType getType(@NotNull String input) {
        switch (input) {
            // Constants
            case "null":
                return VALUE_NULL;
            case "true":
                return VALUE_TRUE;
            case "false":
                return VALUE_FALSE;

                // types
            case "boolean":
                return BOOLEAN;
            case "int8":
                return INT8;
            case "int16":
                return INT16;
            case "int32":
                return INT32;
            case "int64":
                return INT64;
            case "uint8":
                return UINT8;
            case "uint16":
                return UINT16;
            case "uint32":
                return UINT32;
            case "uint64":
                return UINT64;
            case "fp32":
                return FP32;
            case "fp64":
                return FP64;
            case "string":
                return STRING;
            case "object":
                return OBJECT;
            default:
                return IDENTIFIER;
        }
    }

    public String getDefinition() {
        return definition;
    }
}