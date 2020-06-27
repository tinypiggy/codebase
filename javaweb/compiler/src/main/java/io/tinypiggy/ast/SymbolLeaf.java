package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;
import io.tinypiggy.lexer.Token;

public class SymbolLeaf extends AstLeaf {

    public static final int UNKNOWN = -1;

    /**
     * nest 所在作用域和当前作用和与的偏移量
     * index 在作用域的变量数组中的索引
     */
    private int nest, index;
    private ClassInfo classInfo;

    public SymbolLeaf(Token token) {
        super(token);
        this.index = UNKNOWN;
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }

    public String value(){
        return token().getText();
    }

    public int getNest() {
        return nest;
    }

    public void setNest(int nest) {
        this.nest = nest;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ClassInfo classInfo(){
        return classInfo;
    }
}
