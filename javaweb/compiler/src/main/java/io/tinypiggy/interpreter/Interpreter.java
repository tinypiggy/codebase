package io.tinypiggy.interpreter;

import io.tinypiggy.ReturnObj;
import io.tinypiggy.ast.AstTree;
import io.tinypiggy.ast.IncludeExpr;
import io.tinypiggy.ast.NullStmt;
import io.tinypiggy.lexer.Lexer;
import io.tinypiggy.lexer.Token;
import io.tinypiggy.parser.BasicParser;
import io.tinypiggy.parser.IncludeParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class Interpreter {

    // 执行器
    private final static EvaluateVisitor evaluator = new EvaluateVisitor();
    // 符号解析
    private final static LookupVisitor lookupVisitor = new LookupVisitor();
    // 抽象语法树生成
    private final static BasicParser basicParser = new IncludeParser();

    private final static String Path = System.getProperty("user.dir");

    // import 导入的文件
    public static Set<String> resolving = new HashSet<>();
    public static Set<String> resolved = new HashSet<>();


    public static void main(String[] args) {

        if (args == null || args.length < 1){
            throw new RuntimeException("usage: interpreter file");
        }
        // 生成环境
        ResizableOptEnv global = new ResizableOptEnv(null);
        NativeRegister.registerInEnv(global);
        String file = args[0];
        process(global, file);
    }

    public static void process(ResizableOptEnv env, String file){

        BufferedReader reader;
        try {
            resolving.add(file);
            reader = new BufferedReader(new FileReader(Path + File.separator + file + ".sc"));
            Lexer lexer = new Lexer(reader);
            while (lexer.peek(0) != Token.EOF) {
                AstTree ast = basicParser.parse(lexer);
                if (!(ast instanceof NullStmt)) {
                    lookupVisitor.visit(ast, env.getSymbols());
                    Object value = evaluator.visit(ast, env);
                    if (value instanceof IncludeExpr){
                        if (!resolving.contains(((IncludeExpr) value).fileName())
                                && !resolved.contains(((IncludeExpr) value).fileName())){
                            System.out.println("start resolve file " + ((IncludeExpr) value).fileName());
                            process(env, ((IncludeExpr) value).fileName());
                        }
                    }
                    System.out.println(ast.toString() + " => " +  (value instanceof ReturnObj ? ((ReturnObj)value).result : value));
                }
            }
            resolved.add(file);
            resolved.add(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    Interpreter.class.getClassLoader().getResourceAsStream("fib.sc")));
            Lexer lexer = new Lexer(reader);
            ResizableOptEnv global = new ResizableOptEnv(null);
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

