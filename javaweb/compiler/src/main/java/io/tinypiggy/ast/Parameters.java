package io.tinypiggy.ast;

import java.util.List;

public class Parameters extends AstList {

    /**
     * 每个参数在定义域数组中的偏移量数组
     */

    public Parameters(List<AstTree> members) {
        super(members);
    }

    public String name(int i){
        return ((AstLeaf)getMember(i)).token().getText();
    }

    public SymbolLeaf getSymbol(int i){
        return (SymbolLeaf)getMember(i);

    }
}
