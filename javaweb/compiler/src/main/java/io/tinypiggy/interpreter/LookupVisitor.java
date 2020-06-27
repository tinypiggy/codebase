package io.tinypiggy.interpreter;

import io.tinypiggy.ast.*;
import io.tinypiggy.exception.BasicException;
import io.tinypiggy.lexer.StringToken;

import java.util.Map;

/**
 * 优化的方向
 * 1. 确定全局变量的位置
 * 2. 确定函数名义参数的位置 （名义参数的作用于和函数作用域相同）
 * 3. 确定函数作用域中参数位置
 * 4. 确定函数作用域参数个数，用于虚拟机确定栈的长度
 */
public class LookupVisitor implements Visitor<Object> {

    public final static SymbolLeaf SYMBOL_THIS = new SymbolLeaf(new StringToken(0, "this"));

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
        return lookupBin(binaryExpr, environment, false);
    }

    private Object lookupBin(BinaryExpr binaryExpr, Environment environment, boolean isField){
        AstTree left = binaryExpr.left();
        String op = binaryExpr.operator();
        if (op.equals("=")){
            lookupForAssign(left, environment, isField);
        }else {
            left.accept(this, environment);
        }
        binaryExpr.right().accept(this, environment);
        return null;
    }

    private Object lookupForAssign(AstTree left, Environment environment, boolean isField){
        if (left.size() > 1){
            return null;
        }
        if (!(left instanceof SymbolLeaf)){
            throw new BasicException("binary expr left value has wrong type", left);
        }
        Symbols symbols = (Symbols)environment;
        SymbolLeaf leaf = (SymbolLeaf)left;
        Location location;
        // 如果是类变量，不先从外层定义域找
        if (isField){
            location = symbols.putLocal(leaf, null);
        }else {
            location = symbols.put(leaf, null);
        }
        leaf.setIndex(location.index);
        leaf.setNest(location.nest);
        return leaf;
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
        defStmt.setSize(lookupFun(symbols, defStmt.parameters(), defStmt.block(), false));
        return null;
    }

    @Override
    public Object visit(AnonymousFunc anonymousFuc, Environment environment) {
        Symbols symbols = (Symbols)environment;
        anonymousFuc.setSize(lookupFun(symbols, anonymousFuc.parameters(), anonymousFuc.block(), false));
        return null;
    }

    @Override
    public Object visit(DefClass defClass, Environment environment) {
        Symbols symbols = (Symbols)environment;
        defClass.setIndex(symbols.putLocal(defClass.className(), null).index);
        // 解析父类符号
        if(defClass.hasSuperClass()){
            visit(defClass.superClass(), environment);
        }

        // methods 和 fields 分开是为了重用 method，但是 fields 是每个对象一份
        Symbols clazz = new Symbols(symbols);
        Symbols methods = new Symbols(null);
        Symbols fields = new Symbols(null);
        ClassInfo classInfo = new ClassInfo(null, defClass, methods, fields);
        // 解析 classBody 中类变量和方法
        visitClassBody(defClass.getBody(), clazz, methods, classInfo);
        defClass.setClassInfo(classInfo);

        for (Map.Entry<String, Integer> entry : clazz.variables().entrySet()){
            if (!methods.variables().containsKey(entry.getKey())){
                fields.putLocal(new AstLeaf(new StringToken(0, entry.getKey())), null);
            }
        }

        return null;
    }

    private int lookupFun(Symbols symbols, Parameters parameters, BlockStmt block, boolean isMethod){
        Symbols local = new Symbols(symbols);

        // 如果是 类方法 要先用 this 指针占位
        if (isMethod){
            local.putLocal(SYMBOL_THIS, null);
        }

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

    @Override
    public Object visit(NewExpr newExpr, Environment environment) {
        return visitList(newExpr, environment);
    }

    @Override
    public Object visit(Member member, Environment environment) {
        return null;
    }

    /**
     * type 1 : field 2 : method
     */
    private void visitClassBody(ClassBody classBody, Symbols clazz, Symbols methods, ClassInfo classInfo) {
        for (AstTree astTree : classBody){
            if (astTree instanceof DefStmt ){
                clazz.putLocal(((DefStmt) astTree).functionName(), null);

                ((DefStmt) astTree).setIndex(methods.putLocal(((DefStmt) astTree).functionName(), null).index);
                classInfo.getDefStmts().add(((DefStmt) astTree).getIndex(), (DefStmt) astTree);
                ((DefStmt) astTree).setSize(lookupFun(clazz, ((DefStmt) astTree).parameters(), ((DefStmt) astTree).block(), true));

            }else if (astTree instanceof BinaryExpr) {
                lookupBin((BinaryExpr)astTree, clazz, true);
            }
        }
    }
}
