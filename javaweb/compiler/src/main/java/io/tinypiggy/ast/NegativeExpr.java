package io.tinypiggy.ast;

import java.util.List;

public class NegativeExpr extends AstList {

    public NegativeExpr(List<AstTree> members) {
        super(members);
    }

    public AstTree operand(){
        return getMember(0);
    }

    @Override
    public String toString() {
        return "( -" + operand().toString() + ")";
    }
}
