package io.tinypiggy.demo.framework;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Define {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(Appconfig.class);
//        context.setAllowCircularReferences(false);

        context.refresh();
        UserService userService = context.getBean(UserService.class);
        System.out.println(userService.getIndexService());


        /**
         * a object produced process (容器注入流程)
         * scan
         * parse
         * post process
         * call extend
         * traversal parsed map and validate (singleton or prototype)
         * new
         */
//        GenericBeanDefinition definition = new GenericBeanDefinition();
//        definition.setBeanClass(null);
//        definition.setScope("");

        /**
         * spring support circular（recursive） dependency (循环依赖)
         * how to close circular dependency
         *
         * singletonObjects getSingleton(String beanName, ObjectFactory<?> singletonFactory) 中添加
         * singletonFactories   doCreateBean() 中如果允许循环依赖会在这个 map 中放入一个 beanFactory
         * earlySingletonObjects 如果循环依赖了 getSingleton() 方法会 依次从三个 map 中 查找 bean
         *
         * ？ 正常产生的 bean 和 singletonFactories 保存的 factory 产生的 bean 是一样的吗？
         */


        /**
         * doCreateBean()
         * 1. instanceWrapper = createBeanInstance
         * 2. 注入 populateBean
         * 3. 生命周期初始化 initializeBean
         * 4. aop
         * 5. put
         */
    }

    private void test(){

    }

}
