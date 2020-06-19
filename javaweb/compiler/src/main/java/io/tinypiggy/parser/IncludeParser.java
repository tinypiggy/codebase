package io.tinypiggy.parser;

import io.tinypiggy.ast.IncludeExpr;
import io.tinypiggy.ast.SymbolLeaf;

import static io.tinypiggy.parser.Parser.createParser;

public class IncludeParser extends ArrayParser {

    private Parser include = createParser(IncludeExpr.class).skip("import").symbol(SymbolLeaf.class, reserved)
            .repeat(createParser().skip(".").symbol(SymbolLeaf.class, reserved));

    public IncludeParser() {
        super();
        primary.insertChoice(include);
    }
}
