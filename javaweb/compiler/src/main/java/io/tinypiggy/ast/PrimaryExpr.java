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
     * 这里有个点，只有有多个元素的 AstTree List 才能构成 PrimaryExpr 节点，所以 函数节点解析这个就行了
     * @param members 语法树子节点
     * @return 语法树节点
     */
    public static AstTree create(List<AstTree> members){
        return members.size() == 1 ? members.get(0) : new PrimaryExpr(members);
    }
}
