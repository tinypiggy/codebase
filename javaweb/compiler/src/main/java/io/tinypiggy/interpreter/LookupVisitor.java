package io.tinypiggy.interpreter;

import io.tinypiggy.ast.*;
import io.tinypiggy.exception.BasicException;

/**
 * 优化的方向
 * 1. 确定全局变量的位置
 * 2. 确定函数名义参数的位置 （名义参数的作用于和函数作用域相同）
 * 3. 确定函数作用域中参数位置
 * 4. 确定函数作用域参数个数，用于虚拟机确定栈的长度
 */
public class LookupVisitor implements Visitor<Object> {

    @Override
    public Object visit(AstList astList, Environment environment) {
        return visitList(astList, environment);
    }

    private Object visitList(AstList astList, Environment environment){
        Object result = null;
        for (AstTree astTree : astList){
            result = astTree.accept(this, environment);
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
    public Object visit(NegativeExpr negativeExpr, Environment environment) {
        return visitList(negativeExpr, environment);
    }

    @Override
    public Object visit(PrimaryExpr primaryExpr, Environment environment) {
        return visitList(primaryExpr, environment);
    }

    @Override
    public Object visit(SymbolLeaf symbolLeaf, Environment environment) {
        Location location = ((Symbols)environment).get(symbolLeaf);
        if (location == null){
            throw new BasicException("can't find symbol location ", symbolLeaf);
        }
        symbolLeaf.setIndex(location.index);
        symbolLeaf.setNest(location.nest);
        return null;
    }

    @Override
    public Object visit(BinaryExpr binaryExpr, Environment environment) {
        AstTree left = binaryExpr.left();
        String op = binaryExpr.operator();
        if (op.equals("=")){
            lookupForAssign(left, environment);
        }else {
            left.accept(this, environment);
        }
        binaryExpr.right().accept(this, environment);
        return null;
    }

    private Object lookupForAssign(AstTree left, Environment environment){
        if (!(left instanceof SymbolLeaf)){
            throw new BasicException("binary expr left value has wrong type", left);
        }
        Symbols symbols = (Symbols)environment;
        SymbolLeaf leaf = (SymbolLeaf)left;
        Location location = symbols.put(leaf, null);
        leaf.setIndex(location.index);
        leaf.setNest(location.nest);
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
        Symbols symbols = (Symbols)environment;
        defStmt.setIndex(symbols.putLocal(defStmt.functionName(), null).index);
        defStmt.setSize(lookupFun(symbols, defStmt.parameters(), defStmt.block()));
        return null;
    }

    @Override
    public Object visit(AnonymousFunc anonymousFuc, Environment environment) {
        Symbols symbols = (Symbols)environment;
        anonymousFuc.setSize(lookupFun(symbols, anonymousFuc.parameters(), anonymousFuc.block()));
        return null;
    }

    private int lookupFun(Symbols symbols, Parameters parameters, BlockStmt block){
        Symbols local = new Symbols(symbols);

        for (int i = 0; i < parameters.size(); i++){
            SymbolLeaf leaf = parameters.getSymbol(i);
            Location location = local.putLocal(parameters.getSymbol(i), null);
            leaf.setNest(0);
            leaf.setIndex(location.index);
        }
        visitList(block, local);
        return local.size();
    }

    @Override
    public Object visit(ArrayLiteral arrayLiteral, Environment environment) {
        return visitList(arrayLiteral, environment);
    }

    @Override
    public Object visit(IncludeExpr includeExpr, Environment environment) {
        return null;
    }

    @Override
    public Object visit(ReturnExpr returnExpr, Environment environment) {
        return visitList(returnExpr, environment);
    }
}
