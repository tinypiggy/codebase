package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Visitor;

import java.util.List;

public class BinaryExpr extends AstList {

    public BinaryExpr(List<AstTree> members) {
        super(members);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    public AstTree left(){
        return getMember(0);
    }

    public AstTree right(){
        return getMember(2);
    }

    public String operator(){
        return ((AstLeaf)getMember(1)).token().getText();
    }
}
