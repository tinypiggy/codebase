package io.tinypiggy.parser;

import io.tinypiggy.ast.ArrayItem;
import io.tinypiggy.ast.ArrayLiteral;

import static io.tinypiggy.parser.Parser.createParser;

public class ArrayParser extends FuncParser {


    public ArrayParser() {
        reserved.add("]");

        primary.insertChoice(createParser().skip("[").maybe(createParser(ArrayLiteral.class).ast(expr)
                .repeat(createParser().skip(",").ast(expr))).skip("]"));
        postfix.insertChoice(createParser(ArrayItem.class).skip("[").ast(expr).skip("]"));
    }
}
