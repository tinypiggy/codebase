package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;

import java.util.List;

public class DefStmt extends AstList {

    public DefStmt(List<AstTree> members) {
        super(members);
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }

    public String functionName(){
        return ((AstLeaf)getMember(0)).token().getText();
    }

    public Parameters parameters(){
        return (Parameters) getMember(1);
    }

    public BlockStmt block(){
        return (BlockStmt) getMember(2);
    }

    @Override
    public String toString() {
        return "( define function " + functionName() + parameters().toString()
                + "{" + block().toString() + "})";
    }
}
