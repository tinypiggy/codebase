package io.tinypiggy.interpreter;

import io.tinypiggy.ast.*;
import io.tinypiggy.exception.BasicException;

import java.util.Iterator;

public class EvaluateVisitor implements Visitor<Object> {

    private static Environment env = new Environment();

    @Override
    public Object visit(AstList astList) {
        throw new BasicException("AstList resolve fail : " + astList.toString(), astList);
    }

    @Override
    public Object visit(AstLeaf leaf) {
        throw new BasicException("AstLeaf resolve fail : " + leaf.toString(), leaf);
    }

    @Override
    public Object visit(BinaryExpr binaryExpr) {
        AstTree left = binaryExpr.left();
        AstTree right = binaryExpr.right();
        String op = binaryExpr.operator();
        if (op.equals("=")){
            return computeForAssign(left, right);
        }

        return computeOp(left, right, op);
    }

    private Object computeForAssign(AstTree left, AstTree right){
        if (left instanceof SymbolLeaf){
            Object object = right.accept(this);
            env.put(((SymbolLeaf) left).value(), object);
            return object;
        }
        throw new BasicException("left value : " + left.toString() + "is not a symbol", left);
    }

    private Object computeOp(AstTree left, AstTree right, String op){
        Object lValue = left.accept(this);
        Object rValue = right.accept(this);
        if (lValue instanceof Integer && rValue instanceof Integer){
            return computeNumber(lValue, rValue, op);
        }
        if (op.equals("+")){
            if (lValue instanceof String){
                return lValue.toString() + rValue.toString();
            }
        }
        if (op.equals("==")){
            if (lValue == null){
                return rValue == null;
            }
            return lValue.equals(rValue);
        }
        throw new BasicException("binary expression compute fail", left);
    }

    private Object computeNumber(Object lValue, Object rValue, String op){
        switch (op){
            case "+": return (Integer)lValue + (Integer)rValue;
            case "-": return (Integer)lValue - (Integer)rValue;
            case "*": return (Integer)lValue * (Integer)rValue;
            case "/": return (Integer)lValue / (Integer)rValue;
            case "==":return ((Integer) lValue).compareTo((Integer)rValue) == 0;
            case "<": return (Integer)lValue < (Integer)rValue;
            case ">": return (Integer)lValue > (Integer)rValue;
        }
        throw new BasicException("binary compute has not right type");
    }

    @Override
    public Object visit(NegativeExpr negativeExpr) {
        Object object = negativeExpr.operand().accept(this);
        if (object instanceof Integer){
            return -(Integer)object;
        }
        throw new BasicException("NegativeExpr has operand which is not a Integer", negativeExpr);
    }

    @Override
    public Object visit(NullStmt nullStmt) {
        return null;
    }

    @Override
    public Object visit(NumberLeaf numberLeaf) {
        return numberLeaf.value();
    }

    @Override
    public Object visit(StrLeaf strLeaf) {
        return strLeaf.value();
    }

    @Override
    public Object visit(SymbolLeaf symbolLeaf) {
        Object object = env.get(symbolLeaf.value());
        if (object == null){
            throw new BasicException("undefined symbol : " + symbolLeaf.value() + " at " + symbolLeaf.location());
        }
        return object;
    }

    @Override
    public Object visit(PrimaryExpr primaryExpr) {
        Object object = null;
        Iterator<AstTree> members = primaryExpr.members();
        while (members.hasNext()){
            object = members.next().accept(this);
        }
        return object;
    }

    @Override
    public Object visit(IfStmt ifStmt) {
        Object object = null;
        Object condition = ifStmt.condition().accept(this);
        if (condition instanceof Boolean){
            if ((Boolean)condition){
                object = ifStmt.thenBlock().accept(this);
            }
            if (!(Boolean)condition && ifStmt.elseBlock() != null){
                object = ifStmt.thenBlock().accept(this);
            }
        }else {
            throw new BasicException("while statement condition is not boolean");
        }
        return object;
    }

    @Override
    public Object visit(WhileStmt whileStmt) {
        Object object = null;
        while (true) {
            Object condition = whileStmt.condition().accept(this);
            if (condition instanceof Boolean){
                if ((Boolean)condition){
                    object = whileStmt.thenBlock().accept(this);
                }else {
                    break;
                }
            }else {
                throw new RuntimeException("while statement condition is not boolean");
            }
        }
        return object;
    }

    @Override
    public Object visit(BlockStmt blockStmt) {
        Object object = null;
        Iterator<AstTree> members = blockStmt.members();
        while (members.hasNext()){
            object = members.next().accept(this);
        }
        return object;    }
}
