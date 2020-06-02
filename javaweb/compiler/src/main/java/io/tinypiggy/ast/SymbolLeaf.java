package io.tinypiggy.ast;

import io.tinypiggy.lexer.Token;

public class SymbolLeaf extends AstLeaf {

    public SymbolLeaf(Token token) {
        super(token);
    }

    public String value(){
        return token().getText();
    }
}
