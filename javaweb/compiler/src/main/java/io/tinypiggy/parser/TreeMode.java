package io.tinypiggy.parser;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;

import java.util.List;

public class TreeMode implements ParseMode {

    private Parser parser;

    public TreeMode(Parser parser) {
        this.parser = parser;
    }

    @Override
    public AstTree parse(Lexer lexer, List<AstTree> astTrees) throws ParserException {
        return parser.visit(lexer);
    }

    @Override
    public boolean match(Lexer lexer) throws ParserException {
        return false;
    }
}
