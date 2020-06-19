package io.tinypiggy.parser;

import io.tinypiggy.ast.IncludeExpr;
import io.tinypiggy.ast.ReturnExpr;
import io.tinypiggy.ast.SymbolLeaf;

import static io.tinypiggy.parser.Parser.createParser;

public class IncludeParser extends ArrayParser {

    private Parser include = createParser(IncludeExpr.class).skip("import").symbol(SymbolLeaf.class, reserved)
            .repeat(createParser().skip(".").symbol(SymbolLeaf.class, reserved));

    private Parser returnExpr = createParser(ReturnExpr.class).skip("return").ast(expr);

    public IncludeParser() {
        super();
        primary.insertChoice(include);
        simple.insertChoice(returnExpr);
    }
}
