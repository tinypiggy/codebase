package io.tinypiggy.ast;

import java.util.List;

public class WhileStmt extends AstList {

    public WhileStmt(List<AstTree> members) {
        super(members);
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
