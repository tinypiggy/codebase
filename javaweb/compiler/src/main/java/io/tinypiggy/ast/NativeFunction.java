package io.tinypiggy.ast;

import io.tinypiggy.exception.BasicException;

import java.lang.reflect.Method;

public class NativeFunction {

    private int paramNumbers;
    private Method method;
    private String name;

    public NativeFunction(int paramNumbers, Method method, String name) {
        this.paramNumbers = paramNumbers;
        this.method = method;
        this.name = name;
    }

    public int getParamNumbers() {
        return paramNumbers;
    }

    public String getName() {
        return name;
    }

    public Object invoke(Object[] args, AstTree astTree){
        try {
            return method.invoke(null,args);
        } catch (Exception e) {
            throw new BasicException("native function " + name + " invoke failed:", astTree);
        }
    }
}
