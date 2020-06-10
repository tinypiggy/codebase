package io.tinypiggy.interpreter;

import io.tinypiggy.ast.NativeFunction;
import io.tinypiggy.exception.BasicException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Environment {

    private Environment out;
    private Map<String, Object> table = new HashMap<>();

    public Environment(Environment out) {
        super();
        this.out = out;
    }

    public Object get(String key){
        Object value = table.get(key);
        if (value == null && out != null){
            return out.get(key);
        }
        return value;
    }

    public Object put(String key, Object value){
        Environment target = getTargetEnv(key);
        if (target == null){
            return putLocal(key, value);
        }
        return target.putLocal(key, value);
    }

    public Object putLocal(String key, Object value){
        return table.put(key, value);
    }

    private Environment getTargetEnv(String key){
        if (table.get(key) != null){
            return this;
        }else if(out == null){
            return null;
        }
        return out.getTargetEnv(key);
    }

    public Environment getOut() {
        return out;
    }

    public static Environment environment(){
        Environment environment = new Environment(null);
        register(environment, "print", Environment.class, Object.class, String.class);
        return environment;
    }

    public static void print(Object message, String prefix){
        System.out.println(prefix + " " +  message);
    }

    public static void register(Environment environment, String name, Class<?> owner, Class<?>...parameters){
        try {
            Method method = owner.getMethod(name, parameters);
            environment.putLocal(name, new NativeFunction(parameters.length, method, name));
        } catch (NoSuchMethodException e) {
            throw new BasicException("register native function " + name +" failed");
        }
    }

}
