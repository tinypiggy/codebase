package io.tinypiggy.lexer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private BufferedReader reader;

    private String space = "\\s+";
    private String comments = "//.*";
    private String stringLiteral = "\"(\\\\\"|\\\\\\\\|[^\"])*\"";
    private String number = "\\d+";
    private String symbol = "[a-zA-Z][a-zA-Z]";
    private String operator = "\\+|-|\\*|/|%|==|=|\\|\\||&&";

    private String regex = "(" + space + ")|" +
            "(" + comments +        ")|" +
            "(" + stringLiteral +   ")|" +
            "(" + number +          ")|" +
            "(" + symbol +          ")|" +
            "(" + operator +        ")|" +
            "(.*)";
    private Pattern pattern = Pattern.compile(regex);

    private boolean hasMore = false;

    public Lexer(BufferedReader reader){
        this.reader = reader;
    }

    private List<Token> queue = new LinkedList<>();

    public Token read(){
        if (queryToken(0)){
            return queue.remove(0);
        }
        return Token.EOF;
    }

    public Token peek(int i){

        return Token.EOF;
    }

    private boolean queryToken(int i){
        while (i >= queue.size()){
            if (hasMore){
                // todo
            }else {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        if (args.length == 0){
            System.out.println("need a input file\nusage : java lexer fileName");
        }
        String space = "\\s+";
        String comments = "//.*";
        String stringLiteral = "\"(\\\\\"|;|\\\\\\\\|[^\"])*\"";
        String number = "\\d+";
        String symbol = "[a-zA-Z][a-zA-Z0-9]*";
        String operator = "\\+|-|\\*|/|%|==|=|\\|\\||&&|<=|>=|<|>|\\p{Punct}"; // \p{Punct} 标点符号：!"#$%&'()*+,-./:;<=>?@[\]^_{|}~

        String regex = "((" + space + ")|" +
            "(" + comments +        ")|" +
            "(" + stringLiteral +   ")|" +
            "(" + number +          ")|" +
            "(" + symbol +          ")|" +
            "(" + operator +        ")|" +
                "(.*))";

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
            String line;
            Pattern pattern = Pattern.compile(regex);

            while ((line =reader.readLine()) != null){
                Matcher matcher = pattern.matcher(line);
                // region 函数 指定匹配位置前后是透明的
                // 比如 reg = "\\bcar\\b" 匹配一个 单词 car，“cars” 就不能匹配
                // region 函数指定 “acars” 指定匹配位置 为(1, 4)，则不透明时匹配成功，否则失败
                matcher.useTransparentBounds(true);
                matcher.useAnchoringBounds(false);      // 匹配的开头是 region 函数 指定的位置
                int pos = 0, end = line.length();
                while (pos < end){
                    matcher.region(pos, end);
                    if (matcher.lookingAt()){
                        if (matcher.group(2) == null && matcher.group(3) == null){
                            if (matcher.group(4) != null){
                                System.out.println("string : " + matcher.group(4));
                            }
                            if (matcher.group(6) != null){
                                System.out.println("number : " + matcher.group(6));
                            }
                            if (matcher.group(7) != null){
                                System.out.println("symbol : " + matcher.group(7));
                            }
                            if (matcher.group(8) != null){
                                System.out.println("operator : " + matcher.group(8));
                            }
                            if (matcher.group(9) != null){
                                System.out.println("out of grammar : " + matcher.group(9));
                            }
                        }
                        pos = matcher.end();
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
