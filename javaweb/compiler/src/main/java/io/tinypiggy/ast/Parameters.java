package io.tinypiggy.ast;

import java.util.List;

public class Parameters extends AstList {

    public Parameters(List<AstTree> members) {
        super(members);
    }

    String name(int i){
        return ((AstLeaf)getMember(i)).token().getText();
    }
}
