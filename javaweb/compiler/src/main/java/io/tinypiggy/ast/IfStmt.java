package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;

import java.util.List;

public class IfStmt extends AstList {

    public IfStmt(List<AstTree> members) {
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

    public AstTree elsePart(){
        return size() > 2 ? getMember(2) : null;
    }

    @Override
    public String toString() {
        return " if(" + condition().toString() + "){"
                + thenBlock() + "}" +
                (size() > 2 ? " else " + elsePart().toString() + "" : "");
    }
}
