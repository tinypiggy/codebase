package io.tinypiggy.lexer;

public class StringToken extends Token {


    private String literal;

    public StringToken(int lineNumber, String literal){
        super(lineNumber);
        this.literal = literal;
    }

    public Object getText() {
        return literal;
    }

    @Override
    public boolean isString() {
        return true;
    }
}
