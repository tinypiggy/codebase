package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;

import java.util.List;

public class BlockStmt extends AstList {

    public BlockStmt(List<AstTree> members) {
        super(members);
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }
}
