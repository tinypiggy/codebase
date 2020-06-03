package io.tinypiggy.exception;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.lexer.Token;

public class BasicException extends RuntimeException {

    public BasicException(String message) {
        super(message);
    }

    public BasicException(String message, Token token) {
        super(message + ", case in line number: " + token.location());
    }

    public BasicException(String msg, AstTree astTree){
        super(msg + " " + astTree.location());
    }
}
