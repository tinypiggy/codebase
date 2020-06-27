package io.tinypiggy.interpreter;

import io.tinypiggy.ast.AstLeaf;

import java.util.HashMap;
import java.util.Map;

public class Symbols implements Environment {


    private Symbols out;
    private Map<String, Integer> variables;

    public Symbols(Symbols out) {
        this.out = out;
        variables = new HashMap<>();
    }

    public int size() {
        return variables.size();
    }

    private Integer find(String key){
        return variables.get(key);
    }

    public Location get(AstLeaf key, Integer nest){
        Integer index = find(key.token().getText());
        if (index != null){
            return new Location(nest, index);
        }
        if (out != null){
            return out.get(key, nest + 1);
        }
        return null;
    }


    public Map<String, Integer> variables(){
        return variables;
    }

    @Override
    public Location get(AstLeaf key){
        return get(key, 0);
    }

    @Override
    public Location putLocal(AstLeaf key, Object noUse){
        Integer index = find(key.token().getText());
        if (index != null){
            return new Location(0, index);
        }
        index = size();
        variables.put(key.token().getText(), index);
        return new Location(0, index);
    }

    @Override
    public Location put(AstLeaf key, Object noUse){
        Location location = get(key);
        if (location == null){
            return putLocal(key, null);
        }
        return location;
    }

    public void setOut(Symbols out) {
        this.out = out;
    }
}
