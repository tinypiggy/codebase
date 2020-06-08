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

    /**
     * 为 闭包closure 设计的，也可以扩展成对 class 的支持
     * 这个 environment 对象保存了上层 环境的变量，从而实现了闭包
     * @return 所在定义域的环境
     */
    public Environment makeEnvironment() {
        return new Environment(environment);
    }

}
