package io.tinypiggy.ast;

import java.util.List;

public class IfStmt extends AstList {

    public IfStmt(List<AstTree> members) {
        super(members);
    }

    public AstTree condition(){
        return getMember(0);
    }

    public AstTree thenBlock(){
        return getMember(1);
    }

    public AstTree elseBlock(){
        return size() > 2 ? getMember(2) : null;
    }

    @Override
    public String toString() {
        return " if(" + condition().toString() + "){"
                + thenBlock() + "}" +
                (size() > 2 ? "else{" + elseBlock().toString() + "}" : "");
    }
}
