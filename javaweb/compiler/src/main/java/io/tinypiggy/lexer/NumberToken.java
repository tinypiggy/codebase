package io.tinypiggy.lexer;

public class NumberToken extends Token {

    private int value;

    public NumberToken(int lineNumber, int value) {
        super(lineNumber);
        this.value = value;
    }

    public int getNumber() {
        return value;
    }

    @Override
    public boolean isNumber() {
        return true;
    }
}
