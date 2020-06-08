package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;
import io.tinypiggy.lexer.Token;

import java.util.ArrayList;
import java.util.Iterator;

public class AstLeaf extends AstTree {

    private static ArrayList<AstTree> empty = new ArrayList<>();
    private Token token;
    public AstLeaf(Token token){
        this.token = token;
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }

    @Override
    public AstTree getMember(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public Iterator<AstTree> members() {
        return empty.iterator();
    }

    @Override
    public int size() {
        return empty.size();
    }

    @Override
    public String location() {
        return token.getText() + " at line : " + token.location();
    }

    public Token token(){
        return token;
    }

    @Override
    public String toString() {
        return token.getText();
    }
}
