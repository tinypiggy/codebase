package io.tinypiggy.ast;

import io.tinypiggy.lexer.Token;

public class NumberLeaf extends AstLeaf {

    public NumberLeaf(Token token) {
        super(token);
    }

    public int value(){
        return token().getNumber();
    }
}
