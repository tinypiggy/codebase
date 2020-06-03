package io.tinypiggy.parser;

import io.tinypiggy.ast.AstLeaf;
import io.tinypiggy.ast.AstTree;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;
import io.tinypiggy.lexer.Token;

import java.util.List;

public abstract class TokenMode implements ParseMode {

    private AstFactory factory;

    public TokenMode(Class<? extends AstTree> clazz) {
        if (clazz == null){
            clazz = AstLeaf.class;
        }
        factory = AstFactory.get(clazz, Token.class);
    }

    @Override
    public void parse(Lexer lexer, List<AstTree> astTrees) throws ParserException {

        if (match(lexer)){
            Token token = lexer.read();
            astTrees.add(factory.make(token));
        }else {
            throw new ParserException(lexer.read());
        }

    }

    @Override
    public boolean match(Lexer lexer) throws ParserException {
        return test(lexer.peek(0));
    }

    public abstract boolean test(Token token);
}
