package io.tinypiggy.parser;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;
import io.tinypiggy.lexer.Token;

import java.util.List;

public class TokenMode implements ParseMode {

    private String text;

    public TokenMode(String text) {
        this.text = text;
    }

    @Override
    public AstTree parse(Lexer lexer, List<AstTree> astTrees) throws ParserException {

        if (match(lexer)){
            Token token = lexer.read();
            if (text.equals(token.getText())){
            }
        }
        return null;
    }

    @Override
    public boolean match(Lexer lexer) throws ParserException {
        return test(lexer.peek(0));
    }

    public boolean test(Token token){
        return false;
    }
}
