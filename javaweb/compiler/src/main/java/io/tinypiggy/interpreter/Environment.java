package io.tinypiggy.interpreter;

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
        Map<String, Object> target = getRegion(key);

        if (target == null){
            return null;
        }
        return target.get(key);
    }

    public Object put(String key, Object value){

        Map<String, Object> target = getRegion(key);

        if (target == null){
            return putLocal(key, value);
        }
        return target.put(key, value);
    }

    public Object putLocal(String key, Object value){
        return table.put(key, value);
    }

    private Map<String, Object> getRegion(String key){
        Environment current = this;
        while (true){
            Object value = current.table.get(key);
            if (value != null){
                return current.table;
            }
            if (current.out == null){
                return null;
            }
            current = out;
        }
    }

    public Environment getOut() {
        return out;
    }
}
