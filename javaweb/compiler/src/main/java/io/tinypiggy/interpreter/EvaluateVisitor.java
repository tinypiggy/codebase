package io.tinypiggy.interpreter;

import io.tinypiggy.ReturnObj;
import io.tinypiggy.ast.*;
import io.tinypiggy.exception.BasicException;

import java.util.Iterator;

/**
 * 解释器实现类
 * 1. 遍历语法树，进行计算
 */
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
            environment.put((AstLeaf) left, object);
            return object;
        }else if (left.size() > 1 && left.getMember(1) instanceof Member){
            // 暂不考虑类成员是对象
            TinyObject tinyObject = (TinyObject)environment.get((AstLeaf) left.getMember(0));
            Object object = right.accept(this, environment);
            tinyObject.write((SymbolLeaf) left.getMember(1).getMember(0), object);
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
        Object object = environment.get(symbolLeaf);
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
            if (!(Boolean)condition && ifStmt.elsePart() != null ){
                AstTree elsePart = ifStmt.elsePart();
                if (elsePart instanceof IfStmt){
                    object = visit(elsePart, environment);
                }else {
                    object = elsePart.accept(this, environment);
                }
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
            AstTree astTree = members.next();
            object = astTree.accept(this, environment);
            if (astTree instanceof ReturnExpr){
                return new ReturnObj(object);
            }
            if (object instanceof ReturnObj){
                return ((ReturnObj) object).result;
            }
        }
        return object;
    }

    @Override
    public Object visit(DefStmt defStmt, Environment environment){
        // 这里可以不用 putLocal, 为了不改动代码才如此处理
        // environment.put(0, defStmt.getIndex(), new Function());
        return environment.putLocal(defStmt.functionName(),
                new Function(defStmt.parameters(), defStmt.block(), environment,
                        defStmt.functionName().token().getText(), defStmt.getSize()));
    }

    @Override
    public Object visit(AnonymousFunc anonymousFuc, Environment environment) {
        return new Function(anonymousFuc.parameters(), anonymousFuc.block(), environment,
                "anonymous", anonymousFuc.size());
    }

    @Override
    public Object visit(DefClass defClass, Environment environment) {
        OptimizedEnv env = (OptimizedEnv)environment;
        defClass.getClassInfo().setEnvironment(environment);
        return env.put(0, defClass.getIndex(), defClass.getClassInfo());
    }

    public Object visitClassBody(ClassBody classBody, Environment environment) {
        return null;
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
        Iterator<AstTree> iterator = primaryExpr.iterator();
        Object result = iterator.next().accept(this, environment);
        while (iterator.hasNext()){
            AstTree args = iterator.next();
            result = processNext(args, result, environment);
        }
        return result;
    }

    private Object processNext(AstTree postfix, Object prefix, Environment environment){

        if (prefix instanceof TinyObject && (postfix instanceof Member)){
            return ((TinyObject) prefix).read((SymbolLeaf)postfix.getMember(0));
        }

        if ((postfix instanceof Args) && (prefix instanceof TinyMethod)){
            OptimizedEnv funcEnv = (OptimizedEnv)((TinyMethod) prefix).makeEnvironment();
            funcEnv.put(0, 0, ((TinyMethod) prefix).getSelf());
            resolveParams((Function)prefix, (Args)postfix, funcEnv, environment);
            return ((Function)prefix).getBlock().accept(this, funcEnv);
        }

        if (prefix instanceof NativeFunction && (postfix instanceof Args)){
            Args arguments = (Args)postfix;
            NativeFunction nativeFunction = (NativeFunction)prefix;
            return processNative(nativeFunction, arguments, environment);
        }

        if ((postfix instanceof Args) && (prefix instanceof Function)){
            Environment funcEnv = ((Function)prefix).makeEnvironment();
            resolveParams((Function)prefix, (Args)postfix, funcEnv, environment);
            return ((Function)prefix).getBlock().accept(this, funcEnv);
        }

        if (postfix instanceof ArrayItem && prefix instanceof Object[]){
            Object index = ((ArrayItem)postfix).index().accept(this, environment);
            if (!(index instanceof Integer)){
                throw new BasicException("array offset is not a integer: ", postfix);
            }
            return ((Object[])prefix)[(Integer) index];
        }
        throw new BasicException("primary postfix expression cause exception:", postfix);
    }

    private Object processNative(NativeFunction func, Args args, Environment callerEnv){
        if (args.size() < func.getParamNumbers()){
            throw new BasicException("function " + func.getName() + " 入参数量不正确:", args);
        }
        Object[] params = new Object[func.getParamNumbers()];
        for (int i = 0; i < func.getParamNumbers(); i++){
            params[i] = args.getMember(i).accept(this, callerEnv);
        }
        return func.invoke(params, args);
    }

    private void resolveParams(Function function, Args args, Environment funcEnv, Environment callerEnv){
        Parameters parameters = function.getParameters();
        if (args.size() < parameters.size()){
            throw new BasicException("function " + function.getName() + " 入参数量不正确:", args);
        }
        for (int i = 0; i < parameters.size(); i++){
            funcEnv.putLocal(parameters.getSymbol(i), args.getMember(i).accept(this, callerEnv));
        }
    }

    @Override
    public Object visit(ArrayLiteral arrayLiteral, Environment environment) {
        Object[] arr = new Object[arrayLiteral.size()];
        for (int i = 0; i < arrayLiteral.size(); i++){
            arr[i] = arrayLiteral.getMember(i).accept(this, environment);
        }
        return arr;
    }

    @Override
    public Object visit(IncludeExpr includeExpr, Environment environment) {
        return includeExpr;
    }

    @Override
    public Object visit(ReturnExpr returnExpr, Environment environment) {
        return visitAstList(returnExpr, environment);
    }

    @Override
    public Object visit(NewExpr newExpr, Environment environment) {
        Object value = environment.get((AstLeaf) newExpr.getMember(0));
        if (!(value instanceof ClassInfo)){
            throw new BasicException("the class for create object is not found", newExpr);
        }
        OptimizedEnv env = new OptimizedEnv(((ClassInfo) value).environment(), 1);
        TinyObject tinyObject = new TinyObject(env, (ClassInfo) value);
        env.put(0, 0, tinyObject);
        return tinyObject;
    }

    @Override
    public Object visit(Member member, Environment environment) {
        return null;
    }
}
