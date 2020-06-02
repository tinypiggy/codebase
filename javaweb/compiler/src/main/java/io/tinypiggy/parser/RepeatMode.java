package io.tinypiggy.parser;

import io.tinypiggy.ast.AstList;
import io.tinypiggy.ast.AstTree;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;

import java.util.List;

public class RepeatMode implements ParseMode {

    private boolean onlyOnce;
    private Parser parser;

    public RepeatMode(boolean onlyOnce, Parser parser) {
        this.onlyOnce = onlyOnce;
        this.parser = parser;
    }

    @Override
    public void parse(Lexer lexer, List<AstTree> astTrees) throws ParserException {
        while (match(lexer)){
            AstTree astTree = parser.visit(lexer);
            // 这里注意一下，可以去除一下部分 没有意义 的节点
            if (astTree.getClass() != AstList.class || astTree.size() > 0){
                astTrees.add(astTree);
            }
            if (onlyOnce){
                break;
            }
        }
    }

    @Override
    public boolean match(Lexer lexer) throws ParserException {
        return parser.match(lexer);
    }
}
