package io.tinypiggy.parser;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.lexer.Token;

import java.util.HashSet;
import java.util.Set;

public class SymbolMode extends TokenMode {

    Set<String> reserved;

    public SymbolMode(Class<? extends AstTree> clazz, Set<String> reserved) {
        super(clazz);
        this.reserved = reserved == null ? new HashSet<>() : reserved;
    }

    @Override
    public boolean test(Token token) {
        return token.isSymbol() && !reserved.contains(token.getText());
    }
}
