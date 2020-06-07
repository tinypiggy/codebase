package io.tinypiggy.parser;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;

import java.util.List;

public class OrMode implements ParseMode {

    private Parser[] parsers;

    public OrMode(Parser[] parsers) {
        this.parsers = parsers;
    }

    @Override
    public void parse(Lexer lexer, List<AstTree> astTrees) throws ParserException {
        Parser parser = choose(lexer);
        if (parser == null){
            throw new ParserException(lexer.read());
        }
        astTrees.add(parser.visit(lexer));
    }

    @Override
    public boolean match(Lexer lexer) throws ParserException {
        for (Parser parser : parsers){
            if (parser.match(lexer)){
                return true;
            }
        }
        return false;
    }

    private Parser choose(Lexer lexer) throws ParserException{
        for (Parser parser : parsers){
            if (parser.match(lexer)){
                return parser;
            }
        }
        return null;
    }

    public void insert(Parser parser){
        if (parser == null){
            return;
        }
        Parser[] newParsers = new Parser[parsers.length + 1];
        newParsers[0] = parser;
        System.arraycopy(parsers, 0, newParsers, 1, parsers.length);
        parsers = newParsers;
    }
}
