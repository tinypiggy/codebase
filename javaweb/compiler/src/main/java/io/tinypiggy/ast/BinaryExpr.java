package io.tinypiggy.ast;

import java.util.List;

public class BinaryExpr extends AstList {

    public BinaryExpr(List<AstTree> members) {
        super(members);
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
