package io.tinypiggy.parser;

import io.tinypiggy.ast.*;

import static io.tinypiggy.parser.Parser.createParser;

public class FuncParser extends BasicParser {

    private Parser params = createParser(Parameters.class).symbol(SymbolLeaf.class, reserved)
            .repeat(createParser().skip(",").symbol(SymbolLeaf.class, reserved));
    private Parser def = createParser(DefStmt.class).skip("fun").symbol(SymbolLeaf.class, reserved).skip("(").maybe(params).skip(")").ast(block);
    private Parser args = createParser(Args.class).ast(expr)
            .repeat(createParser().skip(",").ast(expr));

    private Parser posix = createParser().skip("(").maybe(args).skip(")");

    private Parser fun = createParser(AnonymousFuc.class).skip("fun").skip("(").maybe(params).skip(")").ast(block);

    public FuncParser() {
        super();

        primary.maybe(posix);
        primary.insertChoice(fun);
        simple.option(args);
        program.insertChoice(def);
    }
}
