package io.tinypiggy.lexer;

import io.tinypiggy.exception.ParserException;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private LineNumberReader reader;

    private String space = "\\s+";
    private String comments = "//.*";
    private String stringLiteral = "\"(\\\\\"|;|\\\\\\\\|[^\"])*\"";
    private String number = "\\d+";
    private String symbol = "[a-zA-Z][a-zA-Z0-9]*";
    private String operator = "\\+|-|\\*|/|%|==|=|\\|\\||&&|<=|>=|<|>|\\p{Punct}"; // \p{Punct} 标点符号：!"#$%&'()*+,-./:;<=>?@[\]^_{|}~
    private String regex = "((" + space + ")|" +
            "(" + comments +        ")|" +
            "(" + number +          ")|" +
            "(" + stringLiteral +   ")|" +
            "(" + symbol + "|" + operator + ")|" +
            "(.*))";

    private Pattern pattern = Pattern.compile(regex);
    private boolean hasMore;
    private List<Token> queue = new LinkedList<>();

    public Lexer(BufferedReader reader){
        this.hasMore = true;
        this.reader = new LineNumberReader(reader);
    }

    public Token read() throws ParserException{
        if (queryToken(0)){
            return queue.remove(0);
        }
        return Token.EOF;
    }

    public Token peek(int i) throws ParserException{
        if (queryToken(i)){
            return queue.get(i);
        }
        return Token.EOF;
    }

    private boolean queryToken(int i) throws ParserException{
        while (i >= queue.size()){
            if (hasMore){
                resolveTokens();
            }else {
                return false;
            }
        }
        return true;
    }

    private void resolveTokens() throws ParserException{
        try {
            String line;
            if ((line = reader.readLine()) == null){
                hasMore = false;
                return;
            }
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
                            queue.add(new NumberToken(reader.getLineNumber(), Integer.parseInt(matcher.group(4))));
                        }
                        if (matcher.group(5) != null){
                            queue.add(new StringToken(reader.getLineNumber(), resolveStringLiteral(matcher.group(5))));
                        }
                        if (matcher.group(7) != null){
                            queue.add(new SymbolToken(reader.getLineNumber(), matcher.group(7)));
                        }
                        if (matcher.group(8) != null){
                            throw new ParserException("");
                        }
                    }
                    pos = matcher.end();
                }
            }
            queue.add(new SymbolToken(reader.getLineNumber(), Token.EOL));
        } catch (IOException e) {
            throw new ParserException(e);
        }
    }

    private String resolveStringLiteral(String literal){
        int length = literal.length() - 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < length; i++){
            char c = literal.charAt(i);
            if (c == '\\' && i + 1 < length){
                char cc = literal.charAt(i + 1);
                if (cc == '\\' || cc == '"'){
                    continue;
                }else if (cc == 'n'){
                    i++;
                    c = '\n';
                }
            }
            builder.append(c);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        if (args.length == 0){
            System.out.println("need a input file\nusage : java lexer fileName");
        }

        LineNumberReader reader;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(args[0])));
            Lexer lexer = new Lexer(reader);
            StringBuilder builder = new StringBuilder();
            String separator = " : ";
            while (lexer.peek(0) != Token.EOF) {
                builder.append(lexer.peek(0).getClass().getSimpleName()).append(separator)
                        .append(lexer.read().getText()).append("\n");
            }
            System.out.println(builder.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
