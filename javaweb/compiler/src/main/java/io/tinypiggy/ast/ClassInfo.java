package io.tinypiggy.ast;

import io.tinypiggy.interpreter.*;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {

    private DefClass defClass;
    private Symbols methods, fields;
    private List<DefStmt> defStmts;
    private ClassInfo parent;
    Environment environment;

    public ClassInfo(Environment env, DefClass defClass, Symbols methods, Symbols fields) {
        this.environment = env;
        this.defClass = defClass;
        if(defClass.hasSuperClass()){
            // 初始化时 env 是 Symbols， 之后会替换成 OptimizedEnv
            assert env instanceof Symbols;
            Location location = ((Symbols) env).get(defClass.superClass());
            parent = (ClassInfo)Interpreter.global.get(0, location.index, defClass.superClass());
        }
        this.fields = fields;
        this.methods = methods;
        defStmts = new ArrayList<>();
    }

    public ClassInfo getParent(){
        return parent;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment environment(){
        return environment;
    }

    public List<DefStmt> getDefStmts() {
        return defStmts;
    }

    public Symbols getMethods() {
        return methods;
    }

    public Symbols getFields() {
        return fields;
    }

    public String toString(){
        return "(class " + defClass.className().value() + ")";
    }
}
