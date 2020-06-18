package io.tinypiggy.interpreter;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.ast.NullStmt;
import io.tinypiggy.lexer.Lexer;
import io.tinypiggy.lexer.Token;
import io.tinypiggy.parser.ArrayParser;
import io.tinypiggy.parser.BasicParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Interpreter {

    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    Interpreter.class.getClassLoader().getResourceAsStream("fib.sc")));
            Lexer lexer = new Lexer(reader);
            BasicParser basicParser = new ArrayParser();
            EvaluateVisitor evaluator = new EvaluateVisitor();
            ResizableOptEnv global = new ResizableOptEnv(null);
            LookupVisitor lookupVisitor = new LookupVisitor();
            NativeRegister.registerInEnv(global);
            while (lexer.peek(0) != Token.EOF) {
                AstTree ast = basicParser.parse(lexer);
                if (!(ast instanceof NullStmt)) {
                    lookupVisitor.visit(ast, global.getSymbols());
                    Object value = evaluator.visit(ast, global);
                    System.out.println(ast.toString() + " => " + value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
