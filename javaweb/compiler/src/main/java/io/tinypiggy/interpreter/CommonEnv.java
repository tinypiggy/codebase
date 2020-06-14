package io.tinypiggy.interpreter;

import io.tinypiggy.ast.AstLeaf;

import java.util.HashMap;
import java.util.Map;

public class CommonEnv implements Environment {

    private CommonEnv out;
    private Map<String, Object> table = new HashMap<>();

    public CommonEnv(CommonEnv out) {
        super();
        this.out = out;
    }

    @Override
    public Object get(AstLeaf key){
        Object value = table.get(key.token().getText());
        if (value == null && out != null){
            return out.get(key);
        }
        return value;
    }

    @Override
    public Object put(AstLeaf key, Object value){
        CommonEnv target = findEnv(key.token().getText());
        if (target == null){
            return putLocal(key, value);
        }
        return target.putLocal(key, value);
    }

    @Override
    public Object putLocal(AstLeaf key, Object value){
        return table.put(key.token().getText(), value);
    }

    private CommonEnv findEnv(String key){
        if (table.get(key) != null){
            return this;
        }else if(out == null){
            return null;
        }
        return out.findEnv(key);
    }

    public CommonEnv getOut() {
        return out;
    }

}
