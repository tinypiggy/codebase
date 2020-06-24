package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;

public class TinyObject {

    ClassInfo classInfo;
    Object[] fields;
    Environment env;

    public TinyObject() {
    }

    public void write(SymbolLeaf leaf, Object value){

    }

    public Object read(SymbolLeaf leaf){
        return null;
    }
}
