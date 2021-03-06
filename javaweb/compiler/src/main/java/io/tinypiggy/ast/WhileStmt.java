package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;

import java.util.List;

public class WhileStmt extends AstList {

    public WhileStmt(List<AstTree> members) {
        super(members);
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }

    public AstTree condition(){
        return getMember(0);
    }

    public AstTree thenBlock(){
        return getMember(1);
    }


    @Override
    public String toString() {
        return " while(" + condition().toString() + "){"
                + thenBlock() + "}";
    }
}
