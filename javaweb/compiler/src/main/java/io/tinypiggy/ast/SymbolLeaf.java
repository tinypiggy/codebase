package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Visitor;
import io.tinypiggy.lexer.Token;

public class SymbolLeaf extends AstLeaf {

    public SymbolLeaf(Token token) {
        super(token);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    public String value(){
        return token().getText();
    }
}
