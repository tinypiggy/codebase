package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Visitor;

import java.util.List;

public class PrimaryExpr extends AstList {

    public PrimaryExpr(List<AstTree> members) {
        super(members);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }


    /**
     * 创建一个 primary expression 节点
     * @param members 语法树子节点
     * @return 语法树节点
     */
    public static AstTree create(List<AstTree> members){
        return members.size() == 1 ? members.get(0) : new PrimaryExpr(members);
    }
}
