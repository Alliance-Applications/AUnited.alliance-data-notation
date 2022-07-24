package main.java.io.alliance.adn.parser;

import io.alliance.adn.element.*;
import main.java.io.alliance.adn.Walkable;
import main.java.io.alliance.adn.exception.ParsingException;
import main.java.io.alliance.adn.lexer.Token;
import main.java.io.alliance.adn.lexer.TokenType;
import lombok.val;

import java.util.LinkedList;
import java.util.List;

public class Parser extends Walkable<Token> {

    public Token match(TokenType type) throws ParsingException {
        val current = consume();

        if(current.getType() != type) {
            throw new ParsingException(String.format("Unexpected token <%s> expected token of type '%s'!", current, type.toString()));
        }

        return current;
    }

    public Parser(Token[] input) {
        super(input);
    }

    public ADNObject parse() throws ParsingException {
        List<ADNElement> elementList = new LinkedList<>();

        while(current().getType() != TokenType.VALUE_EOF) {
            elementList.add(parseElement());
        }

        return new ADNObject("root", elementList);
    }

    private ADNElement parseElement() throws ParsingException {
        val type = consume();

        switch (type.getType()) {
            case BOOLEAN:
                return parseBoolean();
            case INT8:
            case INT16:
            case INT32:
            case INT64:
            case UINT8:
            case UINT16:
            case UINT32:
            case UINT64:
            case FP32:
            case FP64:
                return parseNumeric(type.getType());
            case STRING:
                return parseString();
            case OBJECT:
                return parseObject();
            default:
                throw new ParsingException(String.format("Unexpected token <%s> expected type token!", type));
        }
    }

    private ADNElement parseBoolean() throws ParsingException {
        val name = match(TokenType.IDENTIFIER);
        match(TokenType.EQUALS);

        switch (current().getType()) {
            case VALUE_TRUE:
                consume();
                match(TokenType.SEMICOLON);
                return new ADNBoolean(name.getText(), true);
            case VALUE_FALSE:
                consume();
                match(TokenType.SEMICOLON);
                return new ADNBoolean(name.getText(), false);
            default:
                throw new ParsingException(String.format("Unexpected token <%s> expected boolean constant!", current()));
        }
    }

    private ADNElement parseNumeric(TokenType type) throws ParsingException {
        val name = match(TokenType.IDENTIFIER);
        match(TokenType.EQUALS);
        val value = consume();
        match(TokenType.SEMICOLON);

        switch (type) {
            case INT8:
                return new ADNInt8(name.getText(), Byte.parseByte(value.getText()));
            case INT16:
                return new ADNInt16(name.getText(), (char)Integer.parseInt(value.getText()));
            case INT32:
                return new ADNInt32(name.getText(), Integer.parseInt(value.getText()));
            case INT64:
                return new ADNInt64(name.getText(), Long.parseLong(value.getText()));
            case UINT8:
                return new ADNUInt8(name.getText(), Long.parseUnsignedLong(value.getText()) & 0xFF);
            case UINT16:
                return new ADNUInt16(name.getText(), Long.parseUnsignedLong(value.getText()) & 0xFFFF);
            case UINT32:
                return new ADNUInt32(name.getText(), Long.parseUnsignedLong(value.getText()));
            case UINT64:
                return new ADNUInt64(name.getText(), Long.parseUnsignedLong(value.getText()));
            case FP32:
                return new ADNFP32(name.getText(), Float.parseFloat(value.getText()));
            case FP64:
                return new ADNFP64(name.getText(), Double.parseDouble(value.getText()));
            default:
                throw new ParsingException(String.format("Unexpected token <%s> expected numeric value!", value));
        }
    }

    private ADNElement parseString() throws ParsingException {
        val name = match(TokenType.IDENTIFIER);
        match(TokenType.EQUALS);
        val value = current().getType() != TokenType.VALUE_NULL ? match(TokenType.VALUE_STRING) : consume();
        match(TokenType.SEMICOLON);

        return new ADNString(name.getText(), value.getText());
    }

    private ADNElement parseObject() throws ParsingException {
        val name = match(TokenType.IDENTIFIER);
        match(TokenType.EQUALS);

        if(current().getType() == TokenType.VALUE_NULL) {
            consume();
            match(TokenType.SEMICOLON);
            return new ADNObject(name.getText(), new LinkedList<>());
        }

        match(TokenType.BRACE_OPEN);

        List<ADNElement> elementList = new LinkedList<>();

        while(current().getType() != TokenType.BRACE_CLOSE) {
            elementList.add(parseElement());
        }

        match(TokenType.BRACE_CLOSE);
        match(TokenType.SEMICOLON);

        return new ADNObject(name.getText(), elementList);
    }
}