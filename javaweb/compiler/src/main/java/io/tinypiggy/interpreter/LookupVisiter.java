package io.tinypiggy.interpreter;

import io.tinypiggy.ast.*;

public class LookupVisiter implements Visitor<Object> {

    @Override
    public Object visit(AstList astList, Environment environment) {
        return visitList(astList, environment);
    }

    private Object visitList(AstList astList, Environment environment){
        Object result = null;
        for (AstTree astTree : astList){
            result = astList.accept(this, environment);
        }
        return result;
    }

    @Override
    public Object visit(NumberLeaf numberLeaf, Environment environment) {
        return null;
    }

    @Override
    public Object visit(StrLeaf strLeaf, Environment environment) {
        return null;
    }

    @Override
    public Object visit(SymbolLeaf symbolLeaf, Environment environment) {
        return null;
    }

    @Override
    public Object visit(BinaryExpr binaryExpr, Environment environment) {
        return null;
    }

    @Override
    public Object visit(IfStmt ifStmt, Environment environment) {
        return visitList(ifStmt, environment);
    }

    @Override
    public Object visit(WhileStmt whileStmt, Environment environment) {
        return visitList(whileStmt, environment);
    }

    @Override
    public Object visit(BlockStmt blockStmt, Environment environment) {
        return visitList(blockStmt, environment);
    }

    @Override
    public Object visit(DefStmt defStmt, Environment environment) {
        return null;
    }

    @Override
    public Object visit(AnonymousFunc anonymousFuc, Environment environment) {
        return null;
    }

    @Override
    public Object visit(ArrayLiteral arrayLiteral, Environment environment) {
        return null;
    }
}
