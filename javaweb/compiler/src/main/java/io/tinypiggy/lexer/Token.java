package io.tinypiggy.lexer;

import io.tinypiggy.exception.BasicException;

public abstract class Token {

    public static final Token EOF = new Token(-1){};
    public static final String EOL = ";";

    private int lineNumber;

    public Token(int lineNumber){
        this.lineNumber = lineNumber;
    }

    public int getNumber() {
        throw new BasicException("词法分析出错", this);
    }

    public String getText() {
        return "";
    }

    public boolean isString(){
        return false;
    }

    public boolean isNumber(){
        return false;
    }

    public boolean isSymbol(){
        return false;
    }

    public int location(){
        return lineNumber;
    }
}
