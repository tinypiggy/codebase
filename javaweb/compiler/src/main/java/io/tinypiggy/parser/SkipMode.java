package io.tinypiggy.parser;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;
import io.tinypiggy.lexer.Token;

import java.util.List;

public class SkipMode implements ParseMode {

    private String[] tokens;

    public SkipMode(String[] tokens) {
        this.tokens = tokens;
    }

    @Override
    public void parse(Lexer lexer, List<AstTree> astTrees) throws ParserException {
        if (match(lexer)){
            lexer.read();
            return;
        }
        throw new ParserException(lexer.read());
    }

    @Override
    public boolean match(Lexer lexer) throws ParserException {
        Token token = lexer.peek(0);
        if (token.isSymbol()){
            for (String text : tokens){
                if (text.equals(token.getText())){
                    return true;
                }
            }
        }
        return false;
    }
}
