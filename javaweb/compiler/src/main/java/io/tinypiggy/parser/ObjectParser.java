package io.tinypiggy.parser;

import io.tinypiggy.ast.*;
import io.tinypiggy.lexer.Token;

import static io.tinypiggy.parser.Parser.createParser;


public class ObjectParser extends IncludeParser {

    private Parser declares = createParser().or(def, simple);
    private Parser classBody = createParser(ClassBody.class).skip("{").option(declares)
            .repeat(createParser().skip(Token.EOL).option(declares)).skip("}");

    private Parser defclass = createParser(DefClass.class).skip("class").symbol(SymbolLeaf.class, reserved)
            .option(createParser().skip("extends").symbol(SymbolLeaf.class, reserved))
            .ast(classBody);

    public ObjectParser() {
        super();
        postfix.insertChoice(createParser(Member.class).skip(".").symbol(SymbolLeaf.class, reserved));
        primary.insertChoice(createParser(NewExpr.class).skip("new").symbol(SymbolLeaf.class, reserved));
        program.insertChoice(defclass);
    }
}