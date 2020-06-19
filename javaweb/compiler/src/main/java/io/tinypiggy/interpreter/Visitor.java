package io.tinypiggy.interpreter;

import io.tinypiggy.ast.*;

public interface Visitor<T> {

    default T visit(AstTree tree, Environment environment){
        return tree.accept(this, environment);
    }
    default T visit(AstList astList, Environment environment){
        return astList.accept(this, environment);
    }
    default T visit(AstLeaf leaf, Environment environment){
        return leaf.accept(this, environment);
    }

    default T visit(NegativeExpr negativeExpr, Environment environment){
        return negativeExpr.accept(this, environment);
    }
    default T visit(NullStmt nullStmt, Environment environment){
        return nullStmt.accept(this, environment);
    }
    default T visit(PrimaryExpr primaryExpr, Environment environment){
        return primaryExpr.accept(this, environment);
    }

    T visit(NumberLeaf numberLeaf, Environment environment);
    T visit(StrLeaf strLeaf, Environment environment);
    T visit(SymbolLeaf symbolLeaf, Environment environment);

    T visit(BinaryExpr binaryExpr, Environment environment);
    T visit(IfStmt ifStmt, Environment environment);
    T visit(WhileStmt whileStmt, Environment environment);
    T visit(BlockStmt blockStmt, Environment environment);

    T visit(DefStmt defStmt, Environment environment);
    T visit(AnonymousFunc anonymousFuc, Environment environment);
    T visit(ArrayLiteral arrayLiteral, Environment environment);

    T visit(IncludeExpr includeExpr, Environment environment);
    T visit(ReturnExpr returnExpr, Environment environment);
}
