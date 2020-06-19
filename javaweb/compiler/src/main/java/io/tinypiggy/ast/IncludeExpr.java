package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class IncludeExpr extends AstList {

    public IncludeExpr(List<AstTree> members) {
        super(members);
    }

    public String fileName(){
        Iterator<AstTree> iterator = members();
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        while (iterator.hasNext()){
            if (!isFirst){
                builder.append(File.separator);
            }
            isFirst = false;
            builder.append(((SymbolLeaf)iterator.next()).value());
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return "import a file " + fileName() + ".sc";
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }

}
