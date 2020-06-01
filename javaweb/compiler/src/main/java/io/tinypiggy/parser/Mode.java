package io.tinypiggy.parser;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;

import java.util.List;

public interface Mode {

    AstTree parse(Lexer lexer, List<AstTree> astTrees) throws ParserException;
    boolean match(Lexer lexer) throws ParserException;

}
