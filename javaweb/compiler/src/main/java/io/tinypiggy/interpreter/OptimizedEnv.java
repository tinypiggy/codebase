package io.tinypiggy.interpreter;

import io.tinypiggy.ast.AstLeaf;
import io.tinypiggy.ast.SymbolLeaf;

public class OptimizedEnv implements Environment {

    Environment out;
    Object[] params;

    public OptimizedEnv(Environment out, int initSize) {
        this.out = out;
        params = new Object[initSize];
    }

    @Override
    public Object get(AstLeaf leaf) {
        if (leaf.token().getText().equals("this")){
            return params[0];
        }
        SymbolLeaf symbolLeaf = (SymbolLeaf)leaf;
        int nest = symbolLeaf.getNest();
        int index = symbolLeaf.getIndex();
        return get(nest, index, leaf);
    }

    public Object get(int nest, int index, AstLeaf name){
        if (index == SymbolLeaf.UNKNOWN){
            return getByName(name);
        }
        if (nest == 0){
            return params[index];
        }
        if (out == null){
            throw new RuntimeException();
        }
        return ((OptimizedEnv)out).get(nest - 1, index, name);
    }

    @Override
    public Object put(AstLeaf leaf, Object value) {
        SymbolLeaf symbolLeaf = (SymbolLeaf)leaf;
        int nest = symbolLeaf.getNest();
        int index = symbolLeaf.getIndex();
        if (index == SymbolLeaf.UNKNOWN){
            Location location = putByName(leaf);
            return put(location.nest, location.index, value);
        }
        return put(nest, index, value);
    }

    @Override
    public Object putLocal(AstLeaf key, Object value) {
        return put(0, ((SymbolLeaf)key).getIndex(), value);
    }

    public Object put(int nest, int index, Object value){
        if (nest == 0){
            return params[index] = value;
        }
        if (out == null){
            throw new RuntimeException();
        }
        return ((OptimizedEnv)out).put(nest - 1, index, value);
    }

    public Location putByName(AstLeaf leaf){
        throw new RuntimeException();
    }

    public Object getByName(AstLeaf name){
        throw new RuntimeException();
    }
}
