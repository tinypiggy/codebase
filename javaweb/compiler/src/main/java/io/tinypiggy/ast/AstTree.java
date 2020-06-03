package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Visitor;

import java.util.Iterator;

public abstract class AstTree implements Iterable<AstTree>{

    public abstract <T> T accept(Visitor<T> visitor);

    public abstract AstTree getMember(int i);

    public abstract Iterator<AstTree> members();

    public abstract int size();

    public abstract String location();

    public Iterator<AstTree> iterator() { return members(); }

}
