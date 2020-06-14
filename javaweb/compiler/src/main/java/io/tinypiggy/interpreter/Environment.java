package io.tinypiggy.interpreter;

import io.tinypiggy.ast.AstLeaf;

public interface Environment {


    default Object get(AstLeaf key){
        throw new RuntimeException();
    }

    default Object put(AstLeaf key, Object value){
        throw new RuntimeException();
    }

    default Object putLocal(AstLeaf key, Object value){
        throw new RuntimeException();
    }
}
