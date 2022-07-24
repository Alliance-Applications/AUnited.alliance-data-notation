package main.java.io.alliance.adn.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public final class Token {
    public static final Token EOF = new Token(TokenType.VALUE_EOF, -1, 0, TokenType.VALUE_EOF.getDefinition());

    @NotNull
    private final TokenType type;

    private final int index;

    private final int length;

    @NotNull
    private final String text;

    @Override
    public String toString() {
        return String.format("%s: [ %d->%d, \"%s\" ]", type, index, length, text);
    }
}

/*
object testContainer = {
	uint64 id = 0;
	boolean isTestObject = false;
	string name = "test container\\";
	object testObject = {
		uint64 id = 1;
		boolean isTestObject = true;
		string name = "test object\\";
		object subObject = null;
	}
}
 */