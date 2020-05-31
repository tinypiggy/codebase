package io.tinypiggy.exception;

import io.tinypiggy.lexer.Token;

public class LexerException extends RuntimeException {

    public LexerException(String message) {
        super(message);
    }

    public LexerException(String message, Token token) {
        super(message + ", case in line number: " + token.location());
    }
}
