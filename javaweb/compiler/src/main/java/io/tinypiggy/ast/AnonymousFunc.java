package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;

import java.util.List;

public class AnonymousFunc extends AstList {

    private int size;

    public AnonymousFunc(List<AstTree> members) {
        super(members);
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }

    public Parameters parameters(){
        return (Parameters) getMember(0);
    }

    public BlockStmt block(){
        return (BlockStmt) getMember(1);
    }

    @Override
    public String toString() {
        return "( anonymous function " + parameters().toString()
                + "{" + block().toString() + "})";
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
