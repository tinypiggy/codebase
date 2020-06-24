package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;

import java.util.List;

public class DefClass extends AstList {

    Environment env;

    public DefClass(List<AstTree> members, Environment env) {
        super(members);
        this.env  = env;
    }

    public String className(){
        return ((SymbolLeaf)getMember(0)).value();
    }
    public boolean hasSuperClass(){
        return size() > 2;
    }

    public String superClass(){
        if (hasSuperClass()){
            return ((SymbolLeaf)getMember(1)).value();
        }
        return null;
    }

    public BlockStmt getBody(){
        return ((BlockStmt)getMember(size() - 1));
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }
}
