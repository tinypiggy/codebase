package io.tinypiggy.parser;

/**
 * 运算符优先级
 */
public class OperatorPriority {

    public int priority;
    /**
     * 是否左结合运算符
     * 1 + 2 + 3 左结合
     * a = b = 1 右结合
     */
    public boolean leftAssociate;

    public enum ASSOCIATE {
        LEFT,
        RIGHT
    }

    public OperatorPriority(int priority, boolean leftAssociate) {
        this.priority = priority;
        this.leftAssociate = leftAssociate;
    }


}
