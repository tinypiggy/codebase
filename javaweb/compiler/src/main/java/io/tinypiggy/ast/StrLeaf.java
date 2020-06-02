package io.tinypiggy.ast;

import io.tinypiggy.lexer.Token;

public class StrLeaf extends AstLeaf {

    public StrLeaf(Token token) {
        super(token);
    }

    public String value(){
        return token().getText();
    }
}
