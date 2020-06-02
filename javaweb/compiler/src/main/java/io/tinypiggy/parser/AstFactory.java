package io.tinypiggy.parser;

import io.tinypiggy.ast.AstList;
import io.tinypiggy.ast.AstTree;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public interface AstFactory {

    String createAstMethodName = "create";

    AstTree make0(Object arg) throws Exception;
    default AstTree make(Object arg){
        try {
            return make0(arg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static AstFactory get(Class<? extends AstTree> clazz, Class argType){
        if (clazz == null){
            return null;
        }
        // 约定的create函数生成对象
        try {
            final Method method = clazz.getMethod(createAstMethodName, argType);
            if (method != null){
                return new AstFactory() {
                    @Override
                    public AstTree make0(Object arg) throws Exception {
                        return (AstTree) method.invoke(null, arg);
                    }
                };
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        // 构造函数生成
        try {
            final Constructor constructor = clazz.getConstructor(argType);
            if (constructor != null){
                return new AstFactory() {
                    @Override
                    public AstTree make0(Object arg) throws Exception {
                        return (AstTree) constructor.newInstance(arg);
                    }
                };
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;

    }

    static AstFactory getAstFactoryForAstList(Class<? extends AstTree> clazz){
        AstFactory factory = get(clazz, List.class);
        if (factory == null){
            return new AstFactory() {
                @Override
                public AstTree make0(Object arg) throws Exception {
                    List<AstTree> trees = (ArrayList<AstTree>)arg;
                    return trees.size() == 1 ? trees.get(0) : new AstList(trees);
                }
            };
        }
        return factory;
    }
}
