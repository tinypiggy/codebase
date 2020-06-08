package io.tinypiggy.interpreter;

import io.tinypiggy.ast.*;
import io.tinypiggy.exception.BasicException;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.parser.Parser;

import java.util.Iterator;

public class EvaluateVisitor implements Visitor<Object> {

    @Override
    public Object visit(AstList astList, Environment environment) {
        throw new BasicException("AstList resolve fail : " + astList.toString(), astList);
    }

    @Override
    public Object visit(AstLeaf leaf, Environment environment) {
        throw new BasicException("AstLeaf resolve fail : " + leaf.toString(), leaf);
    }

    @Override
    public Object visit(BinaryExpr binaryExpr, Environment environment) {
        AstTree left = binaryExpr.left();
        AstTree right = binaryExpr.right();
        String op = binaryExpr.operator();
        if (op.equals("=")){
            return computeForAssign(left, right, environment);
        }

        return computeOp(left, right, op, environment);
    }

    private Object computeForAssign(AstTree left, AstTree right, Environment environment){
        if (left instanceof SymbolLeaf){
            Object object = right.accept(this, environment);
            environment.put(((SymbolLeaf) left).value(), object);
            return object;
        }
        throw new BasicException("left value : " + left.toString() + "is not a symbol", left);
    }

    private Object computeOp(AstTree left, AstTree right, String op, Environment environment){
        Object lValue = left.accept(this, environment);
        Object rValue = right.accept(this, environment);
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
    public Object visit(NegativeExpr negativeExpr, Environment environment) {
        Object object = negativeExpr.operand().accept(this, environment);
        if (object instanceof Integer){
            return -(Integer)object;
        }
        throw new BasicException("NegativeExpr has operand which is not a Integer", negativeExpr);
    }

    @Override
    public Object visit(NullStmt nullStmt, Environment environment) {
        return null;
    }

    @Override
    public Object visit(NumberLeaf numberLeaf, Environment environment) {
        return numberLeaf.value();
    }

    @Override
    public Object visit(StrLeaf strLeaf, Environment environment) {
        return strLeaf.value();
    }

    @Override
    public Object visit(SymbolLeaf symbolLeaf, Environment environment) {
        Object object = environment.get(symbolLeaf.value());
        if (object == null){
            throw new BasicException("undefined symbol : " + symbolLeaf.value() + " at " + symbolLeaf.location());
        }
        return object;
    }

    @Override
    public Object visit(IfStmt ifStmt, Environment environment) {
        Object object = null;
        Object condition = ifStmt.condition().accept(this, environment);
        if (condition instanceof Boolean){
            if ((Boolean)condition){
                object = ifStmt.thenBlock().accept(this, environment);
            }
            if (!(Boolean)condition && ifStmt.elseBlock() != null){
                object = ifStmt.thenBlock().accept(this, environment);
            }
        }else {
            throw new BasicException("while statement condition is not boolean");
        }
        return object;
    }

    @Override
    public Object visit(WhileStmt whileStmt, Environment environment) {
        Object object = null;
        while (true) {
            Object condition = whileStmt.condition().accept(this, environment);
            if (condition instanceof Boolean){
                if ((Boolean)condition){
                    object = whileStmt.thenBlock().accept(this, environment);
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
    public Object visit(BlockStmt blockStmt, Environment environment) {
        return visitAstList(blockStmt, environment);
    }

    private Object visitAstList(AstList astTrees, Environment environment){
        Object object = null;
        Iterator<AstTree> members = astTrees.members();
        while (members.hasNext()){
            object = members.next().accept(this, environment);
        }
        return object;
    }

    @Override
    public Object visit(DefStmt defStmt, Environment environment){
        String name = defStmt.functionName();
        return environment.putLocal(name, new Function(defStmt.parameters(), defStmt.block(), environment));
    }

    @Override
    public Object visit(AnonymousFunc anonymousFuc, Environment environment) {
        return new Function(anonymousFuc.parameters(), anonymousFuc.block(), environment);
    }

    /**
     * 这里只处理函数和数组，因为只有一个 成员节点 的 primaryExpr 节点 会被优化成员节点直接取代
     * @param primaryExpr 函数和数组节点
     * @return 计算结果
     */
    @Override
    public Object visit(PrimaryExpr primaryExpr, Environment environment) {
        if (primaryExpr.size() < 2){
            return null;
        }
        Iterator<AstTree> members = primaryExpr.members();
        Object result = members.next().accept(this, environment);
        while (members.hasNext()){
            AstTree args = members.next();
            result = processFunction(args, result, environment);
        }
        return result;
    }

    private Object processFunction(AstTree args, Object func, Environment environment){
        if (!(args instanceof Args) || !(func instanceof Function)){
            throw new BasicException("function run exception:", (AstTree)func);
        }
        Function function = (Function)func;
        Environment funcEnv = function.makeEnvironment();
        Args arguments = (Args)args;
        resolveParams(function, arguments, funcEnv);
        return function.getBlock().accept(this, funcEnv);
    }

    public void resolveParams(Function function, Args args, Environment funcEnv){
        Parameters parameters = function.getParameters();
        for (int i = 0; i < parameters.size(); i++){
            funcEnv.putLocal(parameters.name(i), args.getMember(i).accept(this, funcEnv.getOut()));
        }
    }
}
