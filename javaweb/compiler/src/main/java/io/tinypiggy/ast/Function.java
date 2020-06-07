package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;

public class Function {

    private Parameters parameters;
    private BlockStmt block;

    private Environment environment;

    public Function(Parameters parameters, BlockStmt block, Environment environment) {
        this.parameters = parameters;
        this.block = block;
        this.environment = environment;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public BlockStmt getBlock() {
        return block;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
