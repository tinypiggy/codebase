package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;

import java.util.List;

public class DefClass extends AstList {

    private int index;
    private ClassInfo classInfo;

    public DefClass(List<AstTree> members) {
        super(members);
    }

    public SymbolLeaf className(){
        return (SymbolLeaf)getMember(0);
    }

    public boolean hasSuperClass(){
        return size() > 2;
    }

    public SymbolLeaf superClass(){
        if (hasSuperClass()){
            return (SymbolLeaf)getMember(1);
        }
        return null;
    }

    public ClassBody getBody(){
        return ((ClassBody)getMember(size() - 1));
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public String toString() {
        return "(class " + className().value() +
                (hasSuperClass() ? " extends " + superClass().value() : "") +
                " {" + getBody().toString() + "}";
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }
}
