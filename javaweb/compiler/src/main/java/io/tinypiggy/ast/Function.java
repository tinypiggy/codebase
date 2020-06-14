package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.OptimizedEnv;

public class Function {

    private Parameters parameters;
    private BlockStmt block;
    private String name;
    private Environment environment;
    private int size; // 参数个数，初始化定义域

    public Function(Parameters parameters, BlockStmt block, Environment environment, String name) {
        this.parameters = parameters;
        this.block = block;
        this.environment = environment;
        this.name = name;
    }

    public Function(Parameters parameters, BlockStmt block, Environment environment, String name, int size) {
        this.parameters = parameters;
        this.block = block;
        this.name = name;
        this.environment = environment;
        this.size = size;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public BlockStmt getBlock() {
        return block;
    }

    public String getName() {
        return name;
    }

    /**
     * 1. 函数的作用域的外层定义域 是定义函数时的作用域 而不是执行函数时的作用域
     * 2. 这个设计可以实现 闭包closure 设计的，也可以扩展成对 class 的支持
     * @return 所在定义域的环境
     */
    public Environment makeEnvironment() {
//        return new CommonEnv((CommonEnv)environment);
        return new OptimizedEnv(environment, size);
    }

}
