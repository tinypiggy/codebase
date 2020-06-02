package io.tinypiggy.exception;

import io.tinypiggy.lexer.Token;

public class ParserException extends Exception {

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(Token token) {
        super("symbol ( " + token.getText() + ") resolved error case in line number: " + token.location());
    }
}
