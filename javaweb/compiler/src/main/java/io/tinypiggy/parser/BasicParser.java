package io.tinypiggy.parser;

import io.tinypiggy.ast.*;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;
import io.tinypiggy.lexer.Token;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static io.tinypiggy.parser.Parser.createParser;

public class BasicParser {

    private HashMap<String, OperatorPriority> operators = new HashMap<>();
    private Set<String> reserved = new HashSet<>();

    private Parser expr0 = createParser();
    private Parser primary = createParser(PrimaryExpr.class).or(
            createParser().skip("(").ast(expr0).skip(")"),
            createParser().number(NumberLeaf.class),
            createParser().stringLiteral(StrLeaf.class),
            createParser().symbol(SymbolLeaf.class, reserved)
    );

    private Parser factor = createParser().or(
            createParser(NegativeExpr.class).skip("-").ast(primary),
            createParser().ast(primary)
    );
    private Parser expr = expr0.expression(BinaryExpr.class, factor, operators);

    private Parser simple = createParser(PrimaryExpr.class).ast(expr);
    private Parser statement0 = createParser();
    private Parser block = createParser()
            .skip("{").option(statement0)
            .repeat(createParser().skip(Token.EOL).option(statement0))
            .skip("}");
    private Parser statement = statement0.or(
            createParser(IfStmt.class).skip("if").ast(expr).ast(block).option(createParser().skip("else").ast(block)),
            createParser(WhileStmt.class).skip("while").ast(block),
            simple
    );

    private Parser program = createParser().or(statement, createParser(NullStmt.class)).skip(Token.EOL);

    public AstTree parse(Lexer lexer) throws ParserException{
        return program.visit(lexer);
    }

    public BasicParser() {
        reserved.add(Token.EOL);
        reserved.add(")");
        reserved.add("}");

        operators.put("=", new OperatorPriority(1, false));
        operators.put("==", new OperatorPriority(2, true));
        operators.put(">", new OperatorPriority(2, true));
        operators.put("<", new OperatorPriority(2, true));
        operators.put("+", new OperatorPriority(3, true));
        operators.put("-", new OperatorPriority(3, true));
        operators.put("*", new OperatorPriority(4, true));
        operators.put("/", new OperatorPriority(4, true));
        operators.put("%", new OperatorPriority(4, true));

    }

    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("F:\\git-repo\\codebase\\javaweb\\compiler\\program.script")));
            Lexer lexer = new Lexer(reader);
            BasicParser basicParser = new BasicParser();
            while (lexer.peek(0) != Token.EOF) {
                AstTree ast = basicParser.parse(lexer);
                System.out.println("=> " + ast.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
