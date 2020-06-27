package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;

public class TinyMethod extends Function {

    private TinyObject self;

    public TinyMethod(Parameters parameters, BlockStmt block, Environment environment, String name, int size, TinyObject self) {
        super(parameters, block, environment, name, size);
        this.self = self;
    }

    public TinyObject getSelf() {
        return self;
    }
}
