package io.tinypiggy.parser;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    // 组合字方式
    // 每个 parser 含有一些匹配模式 elements

    private List<Mode> modes;
    private AstFactory astFactory;

    public AstTree visit(Lexer lexer) throws ParserException {
        List<AstTree> astTrees = new ArrayList<>();
        for (Mode mode : modes) {
            mode.parse(lexer, astTrees);
        }
        return astFactory.make(astTrees);
    }

    private static final String createAstMethodName = "create";
    private static interface AstFactory {
        AstTree make0(Object arg) throws Exception;
        default AstTree make(Object arg){
            try {
                return make0(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
