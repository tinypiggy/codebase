package io.tinypiggy.parser;

import io.tinypiggy.ast.*;

import static io.tinypiggy.parser.Parser.createParser;

public class FuncParser extends BasicParser {

    private Parser params = createParser(Parameters.class).symbol(SymbolLeaf.class, reserved)
            .repeat(createParser().skip(",").symbol(SymbolLeaf.class, reserved));
    private Parser def = createParser(DefStmt.class).skip("def").symbol(SymbolLeaf.class, reserved)
                .skip("(").maybe(params).skip(")").ast(block);
    private Parser args = createParser(Args.class).ast(expr)
            .repeat(createParser().skip(",").ast(expr));

    private Parser postfix = createParser().skip("(").maybe(args).skip(")");

    /**
     * 这里不能是和函数定义的前缀不一致，因为我们的程序不回溯，
     */
    private Parser fun = createParser(AnonymousFuc.class).skip("fun")
                .skip("(").maybe(params).skip(")").ast(block);

    public FuncParser() {
        super();

        primary.insertChoice(fun);
        primary.repeat(postfix);
        simple.option(args);
        program.insertChoice(def);
    }
}
