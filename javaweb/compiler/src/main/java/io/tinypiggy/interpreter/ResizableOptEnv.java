package io.tinypiggy.interpreter;

import io.tinypiggy.ast.AstLeaf;
import io.tinypiggy.ast.SymbolLeaf;

import java.util.Arrays;

public class ResizableOptEnv extends OptimizedEnv {

    private Symbols symbols;

    public ResizableOptEnv(Environment out) {
        super(out, 16);
        symbols = new Symbols(null);
    }

    public Symbols getSymbols() {
        return symbols;
    }

    @Override
    public Object put(int nest, int index, Object value) {
        if (nest == 0){
            return putLocal(index, value);
        }
        if (out == null){
            throw new RuntimeException();
        }
        return ((OptimizedEnv)out).put(nest - 1, index, value);
    }

    @Override
    public Object putLocal(AstLeaf key, Object value) {
        Integer index = symbols.putLocal(key, null).index;
        return putLocal(index, value);
    }

    public Object putLocal(int index, Object value){
        if (index >= params.length){
            int newLen = params.length * 2;
            if (index >= newLen)
                newLen = index + 1;
            params = Arrays.copyOf(params, newLen);
        }
        return params[index] = value;
    }

    public Location putByName(AstLeaf leaf){
        return symbols.put(leaf, null);
    }

    @Override
    public Object getByName(AstLeaf name) {
        Location location = symbols.get(name);
        if (location != null){
            SymbolLeaf leaf = (SymbolLeaf)name;
            leaf.setNest(location.nest);
            leaf.setIndex(location.index);
            return get(leaf);
        }
        return null;
    }
}
