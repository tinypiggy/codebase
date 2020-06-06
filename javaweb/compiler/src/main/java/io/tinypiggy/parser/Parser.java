package io.tinypiggy.parser;

import io.tinypiggy.ast.AstTree;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {

    // 组合子（Y-combiner）方式
    // 每个 parser 含有一些匹配模式 elements

    private List<ParseMode> parseModes;
    private AstFactory astFactory;

    private Parser(Parser parser){
        parseModes = parser.parseModes;
        astFactory = parser.astFactory;
    }

    private Parser(Class<? extends AstTree> clazz){
        reset(clazz);
    }

    static Parser createParser(){
        return createParser(null);
    }

    static Parser createParser(Class<? extends AstTree> clazz){
        return new Parser(clazz);
    }

    // 清空可用模式，可匹配任意情况，用于 maybe 方法
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


    public Parser symbol(Class<? extends AstTree> clazz, Set<String> reserved){
        parseModes.add(new SymbolMode(clazz, reserved));
        return this;
    }

    public Parser stringLiteral(Class<? extends AstTree> clazz){
        parseModes.add(new StrMode(clazz));
        return this;
    }

    public Parser number(Class<? extends AstTree> clazz){
        parseModes.add(new NumberMode(clazz));
        return this;
    }

    public Parser skip(String...pattern){
        parseModes.add(new SkipMode(pattern));
        return this;
    }

    // 增加一种已知的匹配模式
    public Parser ast(Parser parser){
        parseModes.add(new TreeMode(parser));
        return this;
    }

    public Parser expression(Class<? extends AstTree> clazz, Parser parser, Map<String, OperatorPriority> operators){
        parseModes.add(new ExprMode(clazz, parser, operators));
        return this;
    }

    // 0次或无数次
    public Parser repeat(Parser parser){
        parseModes.add(new RepeatMode(parser, false));
        return this;
    }

    // 0次或一次
    public Parser option(Parser parser){
        parseModes.add(new RepeatMode(parser, true));
        return this;
    }

    public Parser or(Parser...parsers){
        parseModes.add(new OrMode(parsers));
        return this;
    }

    // 几乎等价于 option 方法
    public Parser maybe(Parser parser){
        Parser other = new Parser(parser);
        other.reset();
        parseModes.add(new OrMode(new Parser[]{parser, other}));
        return this;
    }

    public Parser insertChoice(Parser parser){
        Parser other = new Parser(this);
        reset(null);
        or(parser, other);
        return this;
    }
}
