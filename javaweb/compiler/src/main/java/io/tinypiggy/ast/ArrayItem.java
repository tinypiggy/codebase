package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;

import java.util.List;

public class ArrayItem extends AstList {

    public ArrayItem(List<AstTree> members) {
        super(members);
    }

    public AstTree index(){
        return getMember(0);
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }

    @Override
    public String toString() {
        return "[" + index().toString() + "]";
    }
}
