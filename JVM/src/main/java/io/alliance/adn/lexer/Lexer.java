package io.alliance.adn.lexer;

import io.alliance.adn.Walkable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class Lexer extends Walkable<Character> {

    public Lexer(String input) {
        super(stringToCharacterArray(input));
    }

    @NotNull
    @Contract(pure = true)
    private static Character [] stringToCharacterArray(@NotNull String string) {
        final char[] chars = string.toCharArray();

        final int length = chars.length;
        final Character[] objs = new Character[length];

        for(int i = 0; i < length; i++) {
            objs[i] = chars[i];
        }

        return objs;
    }

    public List<Token> lex() {
        final List<Token> tokens = new LinkedList<>();
        final int maxIndex = input.length;

        while(index < maxIndex) {
            tokens.add(lexToken());
        }

        tokens.add(Token.EOF);
        return tokens;
    }

    private @NotNull Token lexToken() {
        while(Character.isWhitespace(current())) {
            index++;
        }

        TokenType type = TokenType.getSingleCharType(current());

        if(type != null) {
            final String text = String.valueOf(current());
            final int index = this.index;
            this.index++;
            return new Token(type, index, 1, text);
        }

        if(current() == '\"') {
            return lexString();
        }

        if(Character.isDigit(current())) {
            return lexNumber();
        }

        final int startIndex = index;
        final StringBuilder builder = new StringBuilder();

        while(Character.isLetterOrDigit(current()) || current() == '_') {
            builder.append(consume());
        }

        final String text = builder.toString();

        type = TokenType.getType(text);

        return new Token(type, startIndex, index - startIndex, text);
    }

    private Token lexNumber() {
        final int startIndex = index;

        final StringBuilder builder = new StringBuilder();

        while(Character.isDigit(current()) || current() == '.') {
            builder.append(consume());
        }

        final String text = builder.toString();
        final int length = index - startIndex;
        return new Token(TokenType.VALUE_NUMERIC, startIndex, length, text);
    }

    private Token lexString() {
        final int startIndex = index;

        // consume the string initializer
        consume();

        final StringBuilder builder = new StringBuilder();

        while(current() != '\"') {
            if(current() == '\\') {
                if(next() == '\\') {
                    builder.append('\\');
                    index += 2;
                    continue;
                }

                if(next() == '\"') {
                    builder.append('\"');
                    index += 2;
                    continue;
                }

                index++;
                continue;
            }

            builder.append(consume());
        }

        // consume the string finalizer
        consume();

        final String text = builder.toString();
        final int length = index - startIndex;
        return new Token(TokenType.VALUE_STRING, startIndex, length, text);
    }
}