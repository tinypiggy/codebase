package io.tinypiggy.ast;

import java.util.List;

public class AnonymousFuc extends AstList {

    public AnonymousFuc(List<AstTree> members) {
        super(members);
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
}
