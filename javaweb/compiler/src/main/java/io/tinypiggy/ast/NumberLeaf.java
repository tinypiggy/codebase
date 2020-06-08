package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;
import io.tinypiggy.lexer.Token;

public class NumberLeaf extends AstLeaf {

    public NumberLeaf(Token token) {
        super(token);
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }

    public int value(){
        return token().getNumber();
    }
}
