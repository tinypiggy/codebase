package io.tinypiggy.lexer;

public class SymbolToken extends Token {

    private String text;

    public SymbolToken(int lineNumber, String text){
        super(lineNumber);
        this.text = text;
    }

    public Object getText() {
        return text;
    }

    @Override
    public boolean isSymbol() {
        return true;
    }
}
