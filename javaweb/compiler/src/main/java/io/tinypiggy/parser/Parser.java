package io.tinypiggy.parser;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    // 组合字方式
    // 每个 parser 含有一些匹配模式 elements

    private List<ParseMode> parseModes;
    private AstFactory astFactory;

    public Parser(Parser parser){
        parseModes = parser.parseModes;
        astFactory = parser.astFactory;
    }

    public Parser(Class<? extends AstTree> clazz){
        reset(clazz);
    }

    private Parser reset(){
        parseModes = new ArrayList<>();
        return this;
    }

    private Parser reset(Class<? extends AstTree> clazz){
        astFactory = AstFactory.getAstFactoryForAstList(clazz);
        parseModes = new ArrayList<>();
        return this;
    }

    public AstTree visit(Lexer lexer) throws ParserException {
        List<AstTree> astTrees = new ArrayList<>();
        for (ParseMode mode : parseModes) {
            mode.parse(lexer, astTrees);
        }
        return astFactory.make(astTrees);
    }

    /**
     * 如果语法规则为空则匹配，否则匹配第一个单元
     * @param lexer 词法解析器
     * @return 是否匹配相应的语法
     */
    public boolean match(Lexer lexer) throws ParserException{
        if (parseModes.size() == 0){
            return true;
        }
        return parseModes.get(0).match(lexer);
    }

}
