package io.tinypiggy.interpreter;

import io.tinypiggy.ast.AstLeaf;
import io.tinypiggy.ast.NativeFunction;
import io.tinypiggy.ast.SymbolLeaf;
import io.tinypiggy.exception.BasicException;
import io.tinypiggy.lexer.StringToken;

import java.lang.reflect.Method;

public class NativeRegister {

    public static void register(Environment environment, String name, Class<?> owner, Class<?>...parameters){
        try {
            Method method = owner.getMethod(name, parameters);
            AstLeaf leaf = new SymbolLeaf(new StringToken(0, name));
            environment.putLocal(leaf, new NativeFunction(parameters.length, method, name));
        } catch (NoSuchMethodException e) {
            throw new BasicException("register native function " + name +" failed");
        }
    }

    public static void registerInEnv(Environment environment){
        register(environment, "print", NativeRegister.class, Object.class, String.class);
        register(environment, "currentTime", NativeRegister.class);
    }

    public static void print(Object message, String prefix){
        System.out.println(prefix + " " +  message);
    }

    public static int currentTime(){
        return (int)(System.currentTimeMillis()/1000);
    }
}
