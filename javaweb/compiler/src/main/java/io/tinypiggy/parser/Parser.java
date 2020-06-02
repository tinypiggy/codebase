package io.tinypiggy.parser;

import io.tinypiggy.ast.AstList;
import io.tinypiggy.ast.AstTree;
import io.tinypiggy.exception.ParserException;
import io.tinypiggy.lexer.Lexer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {

    // 组合字方式
    // 每个 parser 含有一些匹配模式 elements

    private List<ParseMode> parseModes;
    private AstFactory astFactory;
    private HashMap<String, OperatorPriority> operators = new HashMap<>();

    public Parser(Class<? extends AstTree> clazz){
        astFactory = AstFactory.getAstFactoryForAstList(clazz);
        parseModes = new ArrayList<>();
    }

    public AstTree visit(Lexer lexer) throws ParserException {
        List<AstTree> astTrees = new ArrayList<>();
        for (ParseMode mode : parseModes) {
            mode.parse(lexer, astTrees);
        }
        return astFactory.make(astTrees);
    }

    private static final String createAstMethodName = "create";
    public static abstract class AstFactory {
        public abstract AstTree make0(Object arg) throws Exception;
        private AstTree make(Object arg){
            try {
                return make0(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static AstFactory get(Class<? extends AstTree> clazz, Class argType){
            if (clazz == null){
                return null;
            }
            // 约定的create函数生成对象
            try {
                Method method = clazz.getMethod(createAstMethodName, argType);
                if (method != null){
                    return new AstFactory() {
                        @Override
                        public AstTree make0(Object arg) throws Exception {
                            return (AstTree) method.invoke(null, arg);
                        }
                    };
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            // 构造函数生成
            try {
                Constructor constructor = clazz.getConstructor(argType);
                if (constructor != null){
                    return new AstFactory() {
                        @Override
                        public AstTree make0(Object arg) throws Exception {
                            return (AstTree) constructor.newInstance(arg);
                        }
                    };
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;

        }

        private static AstFactory getAstFactoryForAstList(Class<? extends AstTree> clazz){
            AstFactory factory = get(clazz, List.class);
            if (factory == null){
                return new AstFactory() {
                    @Override
                    public AstTree make0(Object arg) throws Exception {
                        List<AstTree> trees = (ArrayList<AstTree>)arg;
                        return trees.size() == 1 ? trees.get(0) : new AstList(trees);
                    }
                };
            }
            return factory;
        }
    }
}
