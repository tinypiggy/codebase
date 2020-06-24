package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Symbols;

public class ClassInfo {

    Symbols methods, fields;
    DefStmt[] defStmts;
    ClassInfo parent;
    Environment env;

    public ClassInfo() {

    }
}
