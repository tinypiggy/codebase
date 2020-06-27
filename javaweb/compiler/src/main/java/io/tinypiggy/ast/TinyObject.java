package io.tinypiggy.ast;

import io.tinypiggy.exception.BasicException;
import io.tinypiggy.interpreter.Environment;

public class TinyObject {

    private ClassInfo classInfo;
    private Object[] fields;
    private Environment env;
    private TinyObject parent;

    public TinyObject(Environment env, ClassInfo classInfo) {
        this.env = env;
        this.classInfo = classInfo;
        fields = new Object[classInfo.getFields().size()];
        if (classInfo.getParent() != null){
            parent = new TinyObject(env, classInfo.getParent());
        }
    }

    public void write(SymbolLeaf leaf, Object value){
        TinyObject tinyObject = getInFields(leaf);
        if (tinyObject != null){
            tinyObject.getFields()[tinyObject.classInfo.getFields().variables().get(leaf.value())] = value;
            return;
        }
        throw new BasicException("can't fix method", leaf);
    }

    public Object read(SymbolLeaf leaf){
        TinyObject tinyObject = getInFields(leaf);
        if (tinyObject != null){
            return tinyObject.getFields()[tinyObject.classInfo.getFields().variables().get(leaf.value())];
        }
        tinyObject = getInMethods(leaf);
        if (tinyObject != null){
            DefStmt defStmt =  tinyObject.classInfo.getDefStmts().get(tinyObject.classInfo.getMethods().variables().get(leaf.value()));
            return new TinyMethod(defStmt.parameters(), defStmt.block(), env, defStmt.functionName().token().getText(), defStmt.getSize(), tinyObject);
        }
        return null;
    }

    private TinyObject getInFields(SymbolLeaf leaf){
        if(classInfo.getFields().variables().containsKey(leaf.value())){
            return this;
        }
        if (parent != null){
            return parent.getInFields(leaf);
        }
        return null;
    }

    private TinyObject getInMethods(SymbolLeaf leaf){
        if(classInfo.getMethods().variables().containsKey(leaf.value())){
            return this;
        }
        if (parent != null){
            return parent.getInMethods(leaf);
        }
        return null;
    }

    public Object[] getFields() {
        return fields;
    }

    public void setFields(Object[] fields) {
        this.fields = fields;
    }
}
