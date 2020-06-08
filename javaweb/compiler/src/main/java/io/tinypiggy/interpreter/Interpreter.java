package io.tinypiggy.interpreter;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.ast.NullStmt;
import io.tinypiggy.lexer.Lexer;
import io.tinypiggy.lexer.Token;
import io.tinypiggy.parser.BasicParser;
import io.tinypiggy.parser.FuncParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Interpreter {

    public static void main(String[] args) {
        BufferedReader reader;
        try {

            reader = new BufferedReader(new InputStreamReader(
                    Interpreter.class.getClassLoader().getResourceAsStream("./program.script")));
            Lexer lexer = new Lexer(reader);
            BasicParser basicParser = new FuncParser();
            EvaluateVisitor evaluator = new EvaluateVisitor();
            Environment global = Environment.environment();
            while (lexer.peek(0) != Token.EOF) {
                AstTree ast = basicParser.parse(lexer);
                if (!(ast instanceof NullStmt)) {
                    Object value = evaluator.visit(ast, global);
                    System.out.println(ast.toString() + " => " + value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
