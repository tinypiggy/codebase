package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;

import java.util.List;

public class NegativeExpr extends AstList {

    public NegativeExpr(List<AstTree> members) {
        super(members);
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }

    public AstTree operand(){
        return getMember(0);
    }

    @Override
    public String toString() {
        return "( -" + operand().toString() + ")";
    }
}
