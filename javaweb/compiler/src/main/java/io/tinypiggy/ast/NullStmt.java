package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Visitor;

import java.util.List;

public class NullStmt extends AstList {

    public NullStmt(List<AstTree> members) {
        super(members);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
