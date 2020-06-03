package io.tinypiggy.parser;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.lexer.Token;

public class NumberMode extends TokenMode {

    public NumberMode(Class<? extends AstTree> clazz) {
        super(clazz);
    }

    @Override
    public boolean test(Token token) {
        return token.isNumber();
    }

}
