package io.tinypiggy.interpreter;

import io.tinypiggy.ast.*;

public interface Visitor<T> {

    default T visit(AstTree tree){
        return tree.accept(this);
    }
    default T visit(AstList astList){
        return astList.accept(this);
    }
    default T visit(AstLeaf leaf){
        return leaf.accept(this);
    }

    default T visit(NegativeExpr negativeExpr){
        return negativeExpr.accept(this);
    }
    default T visit(NullStmt nullStmt){
        return nullStmt.accept(this);
    }
    default T visit(PrimaryExpr primaryExpr){
        return primaryExpr.accept(this);
    }

    T visit(NumberLeaf numberLeaf);
    T visit(StrLeaf strLeaf);
    T visit(SymbolLeaf symbolLeaf);

    T visit(BinaryExpr binaryExpr);
    T visit(IfStmt ifStmt);
    T visit(WhileStmt whileStmt);
    T visit(BlockStmt blockStmt);

    T visit(DefStmt defStmt);
    T visit(AnonymousFuc anonymousFuc);
}
