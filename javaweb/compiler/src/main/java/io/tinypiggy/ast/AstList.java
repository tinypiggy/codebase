package io.tinypiggy.ast;

import io.tinypiggy.interpreter.Environment;
import io.tinypiggy.interpreter.Visitor;

import java.util.Iterator;
import java.util.List;

public class AstList extends AstTree {

    private List<AstTree> members;
    public AstList(List<AstTree> members){
        this.members = members;
    }

    @Override
    public <T> T accept(Visitor<T> visitor, Environment environment) {
        return visitor.visit(this, environment);
    }

    @Override
    public AstTree getMember(int i) {
        return members.get(i);
    }

    @Override
    public Iterator<AstTree> members() {
        return members.iterator();
    }

    @Override
    public int size() {
        return members.size();
    }

    @Override
    public String location() {
        for (AstTree member : members){
            if (member.location() != null){
                return member.location();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String separator = "";
        builder.append("(");
        for (AstTree member : members){
            builder.append(separator);
            builder.append(member.toString());
            separator = " ";
        }
        builder.append(")");
        return builder.toString();
    }
}
