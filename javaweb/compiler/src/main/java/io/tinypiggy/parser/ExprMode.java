package io.tinypiggy.parser;

import io.tinypiggy.ast.AstLeaf;
import io.tinypiggy.ast.AstTree;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;
import io.tinypiggy.lexer.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExprMode implements ParseMode {

    private Map<String, OperatorPriority> operators;
    private Parser parser;
    private AstFactory factory;

    public ExprMode(Class<? extends AstTree> clazz, Parser parser, Map<String, OperatorPriority> operators) {
        this.operators = operators == null ? new HashMap<>() : operators;
        this.parser = parser;
        this.factory = AstFactory.getAstFactoryForAstList(clazz);
    }

    @Override
    public void parse(Lexer lexer, List<AstTree> astTrees) throws ParserException {

        AstTree expr = parser.visit(lexer);
        while (operators.containsKey(lexer.peek(0).getText())){
            expr = doShiftRight(expr, operators.get(lexer.peek(0).getText()), lexer);
        }
        astTrees.add(expr);
    }

    @Override
    public boolean match(Lexer lexer) throws ParserException {
        return parser.match(lexer);
    }


    private boolean isAssociateLeft(OperatorPriority left, OperatorPriority right){
        if (right.leftAssociate){
            return left.priority >= right.priority;
        }
        return left.priority > right.priority;
    }

    private AstTree doShiftRight(AstTree left, OperatorPriority priority, Lexer lexer) throws ParserException{
        List<AstTree> trees = new ArrayList<>();
        trees.add(left);
        trees.add(new AstLeaf(lexer.read()));
        AstTree right = parser.visit(lexer);
        Token op = lexer.peek(0);
        if (operators.containsKey(op.getText()) && !isAssociateLeft(priority, operators.get(op.getText()))){
            right = doShiftRight(right, operators.get(op.getText()), lexer);
        }
        trees.add(right);
        return factory.make(trees);
    }
}
