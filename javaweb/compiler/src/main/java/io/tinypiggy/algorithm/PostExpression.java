package io.tinypiggy.algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 后缀表达式/逆波兰表示法 Reverse Polish Notation
 */
public class PostExpression {

    private BufferedReader reader;
    private List<String> output = new ArrayList<>();

    public PostExpression() {
    }

    public PostExpression(BufferedReader reader) {
        this.reader = reader;
    }

    public List<String> getOutput() {
        return output;
    }

    public void convert(String line){

        output.clear();

        StringBuilder token = new StringBuilder();

        List<String> operators = new ArrayList<>();

        if (line == null){
            return;
        }

        int length = line.length();

        for (int i = 0; i < length; i++){
            boolean isNumber = false;
            char c = line.charAt(i);
            if (c == ' '){
                continue;
            }
            token.setLength(0);
            token.append(c);
            if ('0' <= c && c <= '9' && i + 1 < length) {
                isNumber = true;
                while ('0' <= line.charAt(i + 1) && line.charAt(i + 1) <= '9') {
                    token.append(line.charAt(i + 1));
                    i++;
                    if (i + 1 >= length) {
                        break;
                    }
                }
            }
            if (isNumber){
                output.add(token.toString());
            }else {
                resolve(token.toString(), operators);
            }
        }

        while (operators.size() > 0) {
            output.add(operators.remove(operators.size() - 1));
        }

    }

    private void resolve(String token, List<String> operators){
        if (token.equals("(")){
            operators.add(token);
            return;
        }
        String top = null;
        if (operators.size() > 0){
            top = operators.get(operators.size() - 1);
        }

        if (top == null){
            operators.add(token);
            return;
        }
        if (token.equals(")")){
            String op = null;
            do {
                op = operators.remove(operators.size() - 1);
                if ("(".equals(op)){
                    break;
                }
                output.add(op);
            }while (true);
            return;
        }

        if (top.equals("(") || ((token.equals("*")|| token.equals("/")) && (top.equals("+") || top.equals("-")))){
            operators.add(token);
        }else {
            output.add(token);
        }
    }

    public int calculate(){
        if (output.size() == 0){
            return 0;
        }
        int result = 0;
        boolean isFirst = true;
        List<String> numbers = new ArrayList<>();
        for (int i = 0; i < output.size(); i++){
            switch (output.get(i)){
                case "+":
                case "-":
                case "/":
                case "*": result = doCalculate(isFirst, output.get(i), result, numbers); isFirst = false; break;
                    default: numbers.add(output.get(i));
            }
        }
        return result;
    }

    private int doCalculate(boolean isFirst, String op, int result, List<String> numbers){
        int right = Integer.parseInt(numbers.remove(numbers.size() - 1));
        if (isFirst){
            result = Integer.parseInt(numbers.remove(numbers.size() - 1));
        }
        switch (op){
            case "+": return result + right;
            case "-": return result - right;
            case "/": return result / right;
            case "*": return result * right;
            default: throw new RuntimeException("运算符错误");
        }
    }

    public static void main(String[] args) {
        PostExpression expression = new PostExpression();
        expression.convert("1 + 2 * (3 + 4)");
        expression.getOutput().forEach(System.out::println);
        System.out.println(expression.calculate());
    }
}
